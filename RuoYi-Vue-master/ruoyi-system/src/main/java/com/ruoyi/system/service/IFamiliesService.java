package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Families;

/**
 * 家庭信息Service接口
 * 
 * @author ruoyi
 * @date 2025-09-07
 */
public interface IFamiliesService 
{
    /**
     * 查询家庭信息
     * 
     * @param familieId 家庭信息主键
     * @return 家庭信息
     */
    public Families selectFamiliesById(Integer familieId);

    /**
     * 查询家庭信息列表
     * 
     * @param families 家庭信息
     * @return 家庭信息集合
     */
    public List<Families> selectFamiliesList(Families families);

    /**
     * 新增家庭信息
     * 
     * @param families 家庭信息
     * @return 结果
     */
    public int insertFamilies(Families families);

    /**
     * 修改家庭信息
     * 
     * @param families 家庭信息
     * @return 结果
     */
    public int updateFamilies(Families families);

    /**
     * 批量删除家庭信息
     * 
     * @param familieIds 需要删除的家庭信息主键集合
     * @return 结果
     */
    public int deleteFamiliesByIds(Integer[] familieIds);

    /**
     * 删除家庭信息信息
     * 
     * @param familieId 家庭信息主键
     * @return 结果
     */
    public int deleteFamiliesById(Integer familieId);

    /**
     * 校验家庭名称是否唯一
     * 
     * @param families 家庭信息
     * @return 结果
     */
    public boolean checkFamilyNameUnique(Families families);
}
