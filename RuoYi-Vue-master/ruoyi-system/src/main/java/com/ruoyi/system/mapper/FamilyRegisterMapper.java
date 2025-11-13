package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.FamilyRegister;

/**
 * 家庭注册申请Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-09
 */
public interface FamilyRegisterMapper 
{
    /**
     * 查询家庭注册申请
     * 
     * @param id 家庭注册申请主键
     * @return 家庭注册申请
     */
    public FamilyRegister selectFamilyRegisterById(Integer id);

    /**
     * 查询家庭注册申请列表
     * 
     * @param familyRegister 家庭注册申请
     * @return 家庭注册申请集合
     */
    public List<FamilyRegister> selectFamilyRegisterList(FamilyRegister familyRegister);

    /**
     * 新增家庭注册申请
     * 
     * @param familyRegister 家庭注册申请
     * @return 结果
     */
    public int insertFamilyRegister(FamilyRegister familyRegister);

    /**
     * 修改家庭注册申请
     * 
     * @param familyRegister 家庭注册申请
     * @return 结果
     */
    public int updateFamilyRegister(FamilyRegister familyRegister);

    /**
     * 删除家庭注册申请
     * 
     * @param id 家庭注册申请主键
     * @return 结果
     */
    public int deleteFamilyRegisterById(Integer id);

    /**
     * 批量删除家庭注册申请
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFamilyRegisterByIds(Integer[] ids);

    /**
     * 根据账号查询注册信息
     * 
     * @param account 账号
     * @return 注册信息
     */
    public FamilyRegister selectFamilyRegisterByAccount(String account);

    /**
     * 检查账号是否已存在
     * 
     * @param account 账号
     * @return 结果
     */
    public int checkAccountExists(String account);
}
