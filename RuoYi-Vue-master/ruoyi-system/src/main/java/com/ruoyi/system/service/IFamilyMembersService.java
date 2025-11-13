package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.FamilyMembers;

/**
 * 家庭成员Service接口
 * 
 * @author ruoyi
 * @date 2025-09-07
 */
public interface IFamilyMembersService 
{
    /**
     * 查询家庭成员
     * 
     * @param id 家庭成员主键
     * @return 家庭成员
     */
    public FamilyMembers selectFamilyMembersById(Integer id);

    /**
     * 查询家庭成员列表
     * 
     * @param familyMembers 家庭成员
     * @return 家庭成员集合
     */
    public List<FamilyMembers> selectFamilyMembersList(FamilyMembers familyMembers);

    /**
     * 新增家庭成员
     * 
     * @param familyMembers 家庭成员
     * @return 结果
     */
    public int insertFamilyMembers(FamilyMembers familyMembers);

    /**
     * 修改家庭成员
     * 
     * @param familyMembers 家庭成员
     * @return 结果
     */
    public int updateFamilyMembers(FamilyMembers familyMembers);

    /**
     * 批量删除家庭成员
     * 
     * @param ids 需要删除的家庭成员主键集合
     * @return 结果
     */
    public int deleteFamilyMembersByIds(Integer[] ids);

    /**
     * 删除家庭成员信息
     * 
     * @param id 家庭成员主键
     * @return 结果
     */
    public int deleteFamilyMembersById(Integer id);

    /**
     * 根据账号查询家庭成员
     * 
     * @param account 登录账号
     * @return 家庭成员信息
     */
    public FamilyMembers selectMemberByAccount(String account);

    /**
     * 校验账号是否唯一
     * 
     * @param familyMembers 家庭成员信息
     * @return 结果
     */
    public boolean checkAccountUnique(FamilyMembers familyMembers);

    /**
     * 根据家庭ID查询成员列表
     * 
     * @param familyId 家庭ID
     * @return 家庭成员集合
     */
    public List<FamilyMembers> selectMembersByFamilyId(Integer familyId);

    /**
     * 根据用户ID查询家庭成员信息
     * 
     * @param userId 用户ID
     * @return 家庭成员信息
     */
    public FamilyMembers selectFamilyMemberByUserId(Long userId);

    /**
     * 检查家庭是否已存在管理员
     * 
     * @param familyId 家庭ID
     * @return 如果已存在管理员返回true，否则返回false
     */
    public boolean checkFamilyHasAdmin(Integer familyId);
}
