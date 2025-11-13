package com.ruoyi.system.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.FamilyRegisterMapper;
import com.ruoyi.system.domain.FamilyRegister;
import com.ruoyi.system.domain.FamilyMembers;
import com.ruoyi.system.domain.Families;
import com.ruoyi.system.service.IFamilyRegisterService;
import com.ruoyi.system.service.IFamilyMembersService;
import com.ruoyi.system.service.IFamiliesService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 家庭注册申请Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-09
 */
@Service
public class FamilyRegisterServiceImpl implements IFamilyRegisterService 
{
    @Autowired
    private FamilyRegisterMapper familyRegisterMapper;

    @Autowired
    private IFamilyMembersService familyMembersService;

    @Autowired
    private IFamiliesService familiesService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询家庭注册申请
     * 
     * @param id 家庭注册申请主键
     * @return 家庭注册申请
     */
    @Override
    public FamilyRegister selectFamilyRegisterById(Integer id)
    {
        return familyRegisterMapper.selectFamilyRegisterById(id);
    }

    /**
     * 查询家庭注册申请列表
     * 
     * @param familyRegister 家庭注册申请
     * @return 家庭注册申请
     */
    @Override
    public List<FamilyRegister> selectFamilyRegisterList(FamilyRegister familyRegister)
    {
        return familyRegisterMapper.selectFamilyRegisterList(familyRegister);
    }

    /**
     * 新增家庭注册申请
     * 
     * @param familyRegister 家庭注册申请
     * @return 结果
     */
    @Override
    public int insertFamilyRegister(FamilyRegister familyRegister)
    {
        familyRegister.setCreateTime(new Date());
        familyRegister.setStatus("0"); // 默认待审批
        return familyRegisterMapper.insertFamilyRegister(familyRegister);
    }

    /**
     * 修改家庭注册申请
     * 
     * @param familyRegister 家庭注册申请
     * @return 结果
     */
    @Override
    public int updateFamilyRegister(FamilyRegister familyRegister)
    {
        return familyRegisterMapper.updateFamilyRegister(familyRegister);
    }

    /**
     * 批量删除家庭注册申请
     * 
     * @param ids 需要删除的家庭注册申请主键
     * @return 结果
     */
    @Override
    public int deleteFamilyRegisterByIds(Integer[] ids)
    {
        return familyRegisterMapper.deleteFamilyRegisterByIds(ids);
    }

    /**
     * 删除家庭注册申请信息
     * 
     * @param id 家庭注册申请主键
     * @return 结果
     */
    @Override
    public int deleteFamilyRegisterById(Integer id)
    {
        return familyRegisterMapper.deleteFamilyRegisterById(id);
    }

    /**
     * 根据账号查询注册信息
     * 
     * @param account 账号
     * @return 注册信息
     */
    @Override
    public FamilyRegister selectFamilyRegisterByAccount(String account)
    {
        return familyRegisterMapper.selectFamilyRegisterByAccount(account);
    }

    /**
     * 检查账号是否唯一
     * 
     * @param account 账号
     * @return 结果
     */
    @Override
    public boolean checkAccountUnique(String account)
    {
        int count = familyRegisterMapper.checkAccountExists(account);
        // 还需要检查family_members表中是否已存在
        FamilyMembers existingMember = familyMembersService.selectMemberByAccount(account);
        return count == 0 && existingMember == null;
    }

    /**
     * 审批注册申请
     * 
     * @param id 申请ID
     * @param status 审批状态（1-通过，2-拒绝）
     * @param approveBy 审批人
     * @param remark 审批备注
     * @return 结果
     */
    @Override
    @Transactional
    public int approveRegister(Integer id, String status, String approveBy, String remark)
    {
        FamilyRegister register = familyRegisterMapper.selectFamilyRegisterById(id);
        if (register == null) {
            return 0;
        }

        // 更新审批状态
        register.setStatus(status);
        register.setApproveTime(new Date());
        register.setApproveBy(approveBy);
        register.setRemark(remark);
        int result = familyRegisterMapper.updateFamilyRegister(register);

        // 如果审批通过，创建家庭和家庭成员记录
        if ("1".equals(status) && result > 0) {
            Integer familyId = null;
            
            // 如果是家庭管理员，需要先创建家庭
            if ("admin".equals(register.getUserType())) {
                // 检查家庭名称是否为空
                if (register.getFamilyName() == null || register.getFamilyName().trim().isEmpty()) {
                    throw new RuntimeException("家庭管理员必须提供家庭名称");
                }
                
                // 检查家庭名称是否已存在
                Families searchFamily = new Families();
                searchFamily.setFamilyName(register.getFamilyName());
                List<Families> existingFamilies = familiesService.selectFamiliesList(searchFamily);
                if (!existingFamilies.isEmpty()) {
                    throw new RuntimeException("家庭名称已存在，请更换家庭名称");
                }
                
                // 创建新家庭
                Families family = new Families();
                family.setFamilyName(register.getFamilyName());
                familiesService.insertFamilies(family);
                familyId = family.getFamilieId();
            } else {
                // 如果是普通成员，查找对应的家庭ID
                if (register.getFamilyName() == null || register.getFamilyName().trim().isEmpty()) {
                    throw new RuntimeException("普通成员必须提供要加入的家庭名称");
                }
                
                Families searchFamily = new Families();
                searchFamily.setFamilyName(register.getFamilyName());
                List<Families> familyList = familiesService.selectFamiliesList(searchFamily);
                
                if (familyList.isEmpty()) {
                    throw new RuntimeException("家庭不存在：" + register.getFamilyName());
                }
                
                familyId = familyList.get(0).getFamilieId();
                
                // 检查该家庭是否已有管理员（普通成员加入时家庭必须已有管理员）
                if (!familyMembersService.checkFamilyHasAdmin(familyId)) {
                    throw new RuntimeException("该家庭还没有管理员，无法加入");
                }
            }

            if (familyId != null) {
                // 如果是管理员注册，再次检查该家庭是否已有管理员
                if ("admin".equals(register.getUserType()) && familyMembersService.checkFamilyHasAdmin(familyId)) {
                    throw new RuntimeException("该家庭已存在管理员，一个家庭只能有一个管理员");
                }
                
                // 创建家庭成员记录
                FamilyMembers member = new FamilyMembers();
                member.setFamilyId(familyId);
                // 如果姓名为空，使用账号作为姓名
                String memberName = register.getName();
                if (memberName == null || memberName.trim().isEmpty()) {
                    memberName = register.getAccount();
                }
                member.setName(memberName);
                member.setGender(register.getGender());
                member.setKinship(register.getKinship());
                member.setOccupation(register.getOccupation());
                member.setAccount(register.getAccount());
                member.setPasswordHash(register.getPasswordHash());
                member.setRole("admin".equals(register.getUserType()) ? "管理员" : "普通成员");
                
                familyMembersService.insertFamilyMembers(member);
                
                // 同时在 sys_user 表中创建用户记录，以支持登录
                SysUser sysUser = new SysUser();
                sysUser.setUserName(register.getAccount());
                // 如果姓名为空，使用账号作为昵称
                String nickName = register.getName();
                if (nickName == null || nickName.trim().isEmpty()) {
                    nickName = register.getAccount();
                }
                sysUser.setNickName(nickName);
                sysUser.setPassword(register.getPasswordHash());
                sysUser.setSex("1".equals(register.getGender()) ? "0" : "1"); // 1=男,2=女 转换为 0=男,1=女
                sysUser.setStatus("0"); // 0=正常
                sysUser.setDelFlag("0"); // 0=未删除
                sysUser.setCreateBy(approveBy);
                sysUser.setCreateTime(new Date());
                sysUser.setRemark("家庭成员注册用户");
                
                // 设置默认部门ID (可以使用默认值，如103)
                sysUser.setDeptId(103L);
                
                sysUserService.insertUser(sysUser);
                
                // 为新用户分配家庭成员角色 (role_id = 101)
                Long[] roleIds = {"admin".equals(register.getUserType()) ? 100L : 101L}; // 100=管理员角色, 101=普通成员角色
                sysUser.setRoleIds(roleIds);
                sysUserService.insertUserAuth(sysUser.getUserId(), roleIds);
            }
        }

        return result;
    }
}
