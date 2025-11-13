package com.ruoyi.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.FamilyMembers;
import com.ruoyi.system.domain.Transactions;
import com.ruoyi.system.service.IFamilyMembersService;
import com.ruoyi.system.service.ITransactionsService;
import com.ruoyi.system.service.IFamilyPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 家庭权限验证服务实现
 * 
 * @author ruoyi
 * @date 2025-09-08
 */
@Service
public class FamilyPermissionServiceImpl implements IFamilyPermissionService
{
    private static final Logger logger = LoggerFactory.getLogger(FamilyPermissionServiceImpl.class);
    
    @Autowired
    private IFamilyMembersService familyMembersService;
    
    @Autowired
    private ITransactionsService transactionsService;
    
    /**
     * 检查当前用户是否为家庭管理员
     */
    @Override
    public boolean isCurrentUserFamilyAdmin()
    {
        FamilyMembers currentMember = getCurrentFamilyMember();
        if (currentMember == null) {
            logger.warn("当前用户未找到家庭成员信息");
            return false;
        }
        
        boolean isAdmin = "管理员".equals(currentMember.getRole());
        logger.info("用户权限检查 - 用户: {}, 角色: {}, 是否管理员: {}", 
                   SecurityUtils.getUsername(), currentMember.getRole(), isAdmin);
        return isAdmin;
    }
    
    /**
     * 获取当前用户的家庭成员信息
     */
    @Override
    public FamilyMembers getCurrentFamilyMember()
    {
        String username = SecurityUtils.getUsername();
        if (username == null) {
            logger.warn("无法获取当前登录用户名");
            return null;
        }
        
        FamilyMembers member = familyMembersService.selectMemberByAccount(username);
        if (member == null) {
            logger.warn("用户 {} 未找到对应的家庭成员记录", username);
        }
        return member;
    }
    
    /**
     * 获取当前用户的家庭ID
     */
    @Override
    public Integer getCurrentUserFamilyId()
    {
        FamilyMembers currentMember = getCurrentFamilyMember();
        return currentMember != null ? currentMember.getFamilyId() : null;
    }
    
    /**
     * 检查指定成员是否属于当前用户的家庭
     */
    @Override
    public boolean isMemberInCurrentUserFamily(Integer memberId)
    {
        if (memberId == null) {
            return false;
        }
        
        Integer currentFamilyId = getCurrentUserFamilyId();
        if (currentFamilyId == null) {
            return false;
        }
        
        FamilyMembers member = familyMembersService.selectFamilyMembersById(memberId);
        return member != null && currentFamilyId.equals(member.getFamilyId());
    }
    
    /**
     * 检查指定交易记录是否属于当前用户的家庭
     */
    @Override
    public boolean isTransactionInCurrentUserFamily(Integer transactionId)
    {
        if (transactionId == null) {
            return false;
        }
        
        Integer currentFamilyId = getCurrentUserFamilyId();
        if (currentFamilyId == null) {
            return false;
        }
        
        Transactions transaction = transactionsService.selectTransactionsById(transactionId);
        return transaction != null && currentFamilyId.equals(transaction.getFamilyId());
    }
    
    /**
     * 检查当前用户是否可以修改指定成员的信息
     */
    @Override
    public boolean canModifyMemberInfo(Integer memberId)
    {
        if (memberId == null) {
            return false;
        }
        
        FamilyMembers currentMember = getCurrentFamilyMember();
        if (currentMember == null) {
            return false;
        }
        
        // 如果修改的是自己的信息，允许
        if (currentMember.getId().equals(memberId)) {
            return true;
        }
        
        // 如果是家庭管理员，且目标成员属于同一家庭，允许
        return isCurrentUserFamilyAdmin() && isMemberInCurrentUserFamily(memberId);
    }
    
    /**
     * 检查当前用户是否可以删除指定成员
     */
    @Override
    public boolean canDeleteMember(Integer memberId)
    {
        if (memberId == null) {
            return false;
        }
        
        FamilyMembers currentMember = getCurrentFamilyMember();
        if (currentMember == null) {
            return false;
        }
        
        // 不能删除自己
        if (currentMember.getId().equals(memberId)) {
            return false;
        }
        
        // 只有家庭管理员可以删除其他成员
        return isCurrentUserFamilyAdmin() && isMemberInCurrentUserFamily(memberId);
    }
}
