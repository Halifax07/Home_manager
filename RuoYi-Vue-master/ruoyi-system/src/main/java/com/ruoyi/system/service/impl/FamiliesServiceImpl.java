package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.FamiliesMapper;
import com.ruoyi.system.mapper.FamilyMembersMapper;
import com.ruoyi.system.domain.Families;
import com.ruoyi.system.service.IFamiliesService;

/**
 * 家庭信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-07
 */
@Service
public class FamiliesServiceImpl implements IFamiliesService 
{
    @Autowired
    private FamiliesMapper familiesMapper;

    @Autowired
    private FamilyMembersMapper familyMembersMapper;

    /**
     * 查询家庭信息
     * 
     * @param familieId 家庭信息主键
     * @return 家庭信息
     */
    @Override
    public Families selectFamiliesById(Integer familieId)
    {
        return familiesMapper.selectFamiliesById(familieId);
    }

    /**
     * 查询家庭信息列表
     * 
     * @param families 家庭信息
     * @return 家庭信息
     */
    @Override
    public List<Families> selectFamiliesList(Families families)
    {
        return familiesMapper.selectFamiliesList(families);
    }

    /**
     * 新增家庭信息
     * 
     * @param families 家庭信息
     * @return 结果
     */
    @Override
    public int insertFamilies(Families families)
    {
        return familiesMapper.insertFamilies(families);
    }

    /**
     * 修改家庭信息
     * 
     * @param families 家庭信息
     * @return 结果
     */
    @Override
    public int updateFamilies(Families families)
    {
        return familiesMapper.updateFamilies(families);
    }

    /**
     * 批量删除家庭信息
     * 
     * @param familieIds 需要删除的家庭信息主键
     * @return 结果
     */
    @Override
    public int deleteFamiliesByIds(Integer[] familieIds)
    {
        return familiesMapper.deleteFamiliesByIds(familieIds);
    }

    /**
     * 删除家庭信息信息
     * 
     * @param familieId 家庭信息主键
     * @return 结果
     */
    @Override
    public int deleteFamiliesById(Integer familieId)
    {
        return familiesMapper.deleteFamiliesById(familieId);
    }

    /**
     * 校验家庭名称是否唯一
     * 
     * @param families 家庭信息
     * @return 结果
     */
    @Override
    public boolean checkFamilyNameUnique(Families families)
    {
        Integer familieId = StringUtils.isNull(families.getFamilieId()) ? -1 : families.getFamilieId();
        Families info = familiesMapper.checkFamilyNameUnique(families.getFamilyName());
        if (StringUtils.isNotNull(info) && info.getFamilieId().intValue() != familieId.intValue())
        {
            return false;
        }
        return true;
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
