package com.ruoyi.system.service;

import com.ruoyi.system.domain.FamilyMembers;

/**
 * 家庭权限验证服务接口
 * 
 * @author ruoyi
 * @date 2025-09-08
 */
public interface IFamilyPermissionService
{
    /**
     * 检查当前用户是否为家庭管理员
     * 
     * @return 是否为家庭管理员
     */
    boolean isCurrentUserFamilyAdmin();
    
    /**
     * 获取当前用户的家庭成员信息
     * 
     * @return 家庭成员信息
     */
    FamilyMembers getCurrentFamilyMember();
    
    /**
     * 获取当前用户的家庭ID
     * 
     * @return 家庭ID
     */
    Integer getCurrentUserFamilyId();
    
    /**
     * 检查指定成员是否属于当前用户的家庭
     * 
     * @param memberId 成员ID
     * @return 是否属于同一家庭
     */
    boolean isMemberInCurrentUserFamily(Integer memberId);
    
    /**
     * 检查指定交易记录是否属于当前用户的家庭
     * 
     * @param transactionId 交易记录ID
     * @return 是否属于同一家庭
     */
    boolean isTransactionInCurrentUserFamily(Integer transactionId);
    
    /**
     * 检查当前用户是否可以修改指定成员的信息
     * 
     * @param memberId 成员ID
     * @return 是否可以修改
     */
    boolean canModifyMemberInfo(Integer memberId);
    
    /**
     * 检查当前用户是否可以删除指定成员
     * 
     * @param memberId 成员ID
     * @return 是否可以删除
     */
    boolean canDeleteMember(Integer memberId);
}
