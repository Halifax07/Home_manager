package com.ruoyi.framework.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.RegisterBody;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.IFamilyRegisterService;
import com.ruoyi.system.domain.FamilyRegister;

/**
 * 注册校验方法
 * 
 * @author ruoyi
 */
@Component
public class SysRegisterService
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private IFamilyRegisterService familyRegisterService;

    /**
     * 注册
     */
    public String register(RegisterBody registerBody)
    {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        String userType = registerBody.getUserType();

        // 验证码开关
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid());
        }

        if (StringUtils.isEmpty(username))
        {
            msg = "用户名不能为空";
        }
        else if (StringUtils.isEmpty(password))
        {
            msg = "用户密码不能为空";
        }
        else if (StringUtils.isEmpty(userType))
        {
            msg = "用户类型不能为空";
        }
        else if (!"admin".equals(userType) && !"member".equals(userType))
        {
            msg = "用户类型只能是admin或member";
        }
        else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            msg = "账户长度必须在2到20个字符之间";
        }
        else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            msg = "密码长度必须在5到20个字符之间";
        }
        else if (!familyRegisterService.checkAccountUnique(username))
        {
            msg = "注册失败，账号已存在";
        }
        else
        {
            try {
                // 创建家庭注册申请记录（状态为待审批）
                FamilyRegister familyRegister = new FamilyRegister();
                familyRegister.setAccount(username);
                familyRegister.setPasswordHash(SecurityUtils.encryptPassword(password));
                familyRegister.setName(registerBody.getName());
                familyRegister.setGender(registerBody.getGender());
                familyRegister.setKinship(registerBody.getKinship());
                familyRegister.setOccupation(registerBody.getOccupation());
                familyRegister.setUserType(userType);
                familyRegister.setFamilyName(registerBody.getFamilyName());
                
                int insertResult = familyRegisterService.insertFamilyRegister(familyRegister);
                if (insertResult <= 0) {
                    msg = "注册失败，请联系系统管理员";
                } else {
                    // 检查ID是否正确设置
                    Integer registerId = familyRegister.getId();
                    if (registerId == null) {
                        // 如果ID为空，通过账号查询记录
                        FamilyRegister insertedRegister = familyRegisterService.selectFamilyRegisterByAccount(username);
                        if (insertedRegister != null) {
                            registerId = insertedRegister.getId();
                        }
                    }
                    
                    if (registerId != null) {
                        // 立即自动审批通过，使用户可以马上登录
                        int approveResult = familyRegisterService.approveRegister(
                            registerId, // 使用获取到的记录ID
                            "1", // 状态：1=通过
                            "system", // 审批人：系统自动审批
                            "新用户注册自动审批通过"
                        );
                        
                        if (approveResult > 0) {
                            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.REGISTER, 
                                MessageUtils.message("user.register.success")));
                            msg = ""; // 注册成功，返回空字符串表示无错误
                        } else {
                            msg = "注册失败，自动审批处理出错";
                        }
                    } else {
                        msg = "注册失败，无法获取注册记录ID";
                    }
                }
            } catch (Exception e) {
                msg = "注册失败：" + e.getMessage();
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     * 
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            throw new CaptchaException();
        }
    }
}
