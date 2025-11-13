package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.Transactions;

/**
 * 收支记录Service接口
 * 
 * @author ruoyi
 * @date 2025-09-07
 */
public interface ITransactionsService 
{
    /**
     * 查询收支记录
     * 
     * @param flowId 收支记录主键
     * @return 收支记录
     */
    public Transactions selectTransactionsById(Integer flowId);

    /**
     * 查询收支记录列表
     * 
     * @param transactions 收支记录
     * @return 收支记录集合
     */
    public List<Transactions> selectTransactionsList(Transactions transactions);

    /**
     * 新增收支记录
     * 
     * @param transactions 收支记录
     * @return 结果
     */
    public int insertTransactions(Transactions transactions);

    /**
     * 修改收支记录
     * 
     * @param transactions 收支记录
     * @return 结果
     */
    public int updateTransactions(Transactions transactions);

    /**
     * 批量删除收支记录
     * 
     * @param flowIds 需要删除的收支记录主键集合
     * @return 结果
     */
    public int deleteTransactionsByIds(Integer[] flowIds);

    /**
     * 删除收支记录信息
     * 
     * @param flowId 收支记录主键
     * @return 结果
     */
    public int deleteTransactionsById(Integer flowId);

    /**
     * 根据家庭ID查询收支记录
     * 
     * @param familyId 家庭ID
     * @return 收支记录集合
     */
    public List<Transactions> selectTransactionsByFamilyId(Integer familyId);

    /**
     * 根据家庭成员ID查询收支记录
     * 
     * @param memberId 家庭成员ID
     * @return 收支记录集合
     */
    public List<Transactions> selectTransactionsByMemberId(Integer memberId);

    /**
     * 根据家庭ID和成员ID查询收支记录
     * 
     * @param familyId 家庭ID
     * @param memberId 成员ID
     * @return 收支记录集合
     */
    public List<Transactions> selectTransactionsByFamilyAndMember(Integer familyId, Integer memberId);
}
