package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.FamilyMembersMapper;
import com.ruoyi.system.domain.FamilyMembers;
import com.ruoyi.system.service.IFamilyMembersService;

/**
 * 家庭成员Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-01
 */
@Service
public class FamilyMembersServiceImpl implements IFamilyMembersService 
{
    @Autowired
    private FamilyMembersMapper familyMembersMapper;

    /**
     * 查询家庭成员
     * 
     * @param id 家庭成员主键
     * @return 家庭成员
     */
    @Override
    public FamilyMembers selectFamilyMembersById(Integer id)
    {
        return familyMembersMapper.selectFamilyMembersById(id);
    }

    /**
     * 查询家庭成员列表
     * 
     * @param familyMembers 家庭成员
     * @return 家庭成员
     */
    @Override
    public List<FamilyMembers> selectFamilyMembersList(FamilyMembers familyMembers)
    {
        return familyMembersMapper.selectFamilyMembersList(familyMembers);
    }

    /**
     * 新增家庭成员
     * 
     * @param familyMembers 家庭成员
     * @return 结果
     */
    @Override
    public int insertFamilyMembers(FamilyMembers familyMembers)
    {
        return familyMembersMapper.insertFamilyMembers(familyMembers);
    }

    /**
     * 修改家庭成员
     * 
     * @param familyMembers 家庭成员
     * @return 结果
     */
    @Override
    public int updateFamilyMembers(FamilyMembers familyMembers)
    {
        return familyMembersMapper.updateFamilyMembers(familyMembers);
    }

    /**
     * 批量删除家庭成员
     * 
     * @param ids 需要删除的家庭成员主键
     * @return 结果
     */
    @Override
    public int deleteFamilyMembersByIds(Integer[] ids)
    {
        return familyMembersMapper.deleteFamilyMembersByIds(ids);
    }

    /**
     * 删除家庭成员信息
     * 
     * @param id 家庭成员主键
     * @return 结果
     */
    @Override
    public int deleteFamilyMembersById(Integer id)
    {
        return familyMembersMapper.deleteFamilyMembersById(id);
    }

    /**
     * 根据家庭ID查询成员列表
     * 
     * @param familyId 家庭ID
     * @return 家庭成员列表
     */
    @Override
    public List<FamilyMembers> selectMembersByFamilyId(Integer familyId)
    {
        return familyMembersMapper.selectMembersByFamilyId(familyId);
    }

    /**
     * 根据账号查询成员
     * 
     * @param account 账号
     * @return 家庭成员
     */
    @Override
    public FamilyMembers selectMemberByAccount(String account)
    {
        return familyMembersMapper.selectMemberByAccount(account);
    }

    /**
     * 校验账号是否唯一
     * 
     * @param familyMembers 家庭成员信息
     * @return 结果
     */
    @Override
    public boolean checkAccountUnique(FamilyMembers familyMembers)
    {
        FamilyMembers member = familyMembersMapper.checkAccountUnique(familyMembers.getAccount());
        if (member != null && !member.getId().equals(familyMembers.getId()))
        {
            return false; // 账号已存在
        }
        return true; // 账号可用
    }

    /**
     * 根据用户ID查询家庭成员信息
     * 
     * @param userId 用户ID
     * @return 家庭成员信息
     */
    @Override
    public FamilyMembers selectFamilyMemberByUserId(Long userId)
    {
        return familyMembersMapper.selectFamilyMemberByUserId(userId);
    }

    /**
     * 检查家庭是否已存在管理员
     * 
     * @param familyId 家庭ID
     * @return 如果已存在管理员返回true，否则返回false
     */
    @Override
    public boolean checkFamilyHasAdmin(Integer familyId)
    {
        return familyMembersMapper.checkFamilyHasAdmin(familyId) > 0;
    }
}
