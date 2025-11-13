package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.FamilyRegister;

/**
 * 家庭注册申请Service接口
 * 
 * @author ruoyi
 * @date 2025-09-09
 */
public interface IFamilyRegisterService 
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
     * 批量删除家庭注册申请
     * 
     * @param ids 需要删除的家庭注册申请主键集合
     * @return 结果
     */
    public int deleteFamilyRegisterByIds(Integer[] ids);

    /**
     * 删除家庭注册申请信息
     * 
     * @param id 家庭注册申请主键
     * @return 结果
     */
    public int deleteFamilyRegisterById(Integer id);

    /**
     * 根据账号查询注册信息
     * 
     * @param account 账号
     * @return 注册信息
     */
    public FamilyRegister selectFamilyRegisterByAccount(String account);

    /**
     * 检查账号是否唯一
     * 
     * @param account 账号
     * @return 结果
     */
    public boolean checkAccountUnique(String account);

    /**
     * 审批注册申请
     * 
     * @param id 申请ID
     * @param status 审批状态（1-通过，2-拒绝）
     * @param approveBy 审批人
     * @param remark 审批备注
     * @return 结果
     */
    public int approveRegister(Integer id, String status, String approveBy, String remark);
}
