package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TransactionsMapper;
import com.ruoyi.system.domain.Transactions;
import com.ruoyi.system.service.ITransactionsService;

/**
 * 收支记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-07
 */
@Service
public class TransactionsServiceImpl implements ITransactionsService 
{
    @Autowired
    private TransactionsMapper transactionsMapper;

    /**
     * 查询收支记录
     * 
     * @param flowId 收支记录主键
     * @return 收支记录
     */
    @Override
    public Transactions selectTransactionsById(Integer flowId)
    {
        return transactionsMapper.selectTransactionsById(flowId);
    }

    /**
     * 查询收支记录列表
     * 
     * @param transactions 收支记录
     * @return 收支记录
     */
    @Override
    public List<Transactions> selectTransactionsList(Transactions transactions)
    {
        return transactionsMapper.selectTransactionsList(transactions);
    }

    /**
     * 新增收支记录
     * 
     * @param transactions 收支记录
     * @return 结果
     */
    @Override
    public int insertTransactions(Transactions transactions)
    {
        return transactionsMapper.insertTransactions(transactions);
    }

    /**
     * 修改收支记录
     * 
     * @param transactions 收支记录
     * @return 结果
     */
    @Override
    public int updateTransactions(Transactions transactions)
    {
        return transactionsMapper.updateTransactions(transactions);
    }

    /**
     * 批量删除收支记录
     * 
     * @param flowIds 需要删除的收支记录主键
     * @return 结果
     */
    @Override
    public int deleteTransactionsByIds(Integer[] flowIds)
    {
        return transactionsMapper.deleteTransactionsByIds(flowIds);
    }

    /**
     * 删除收支记录信息
     * 
     * @param flowId 收支记录主键
     * @return 结果
     */
    @Override
    public int deleteTransactionsById(Integer flowId)
    {
        return transactionsMapper.deleteTransactionsById(flowId);
    }

    /**
     * 根据家庭ID查询收支记录
     * 
     * @param familyId 家庭ID
     * @return 收支记录集合
     */
    @Override
    public List<Transactions> selectTransactionsByFamilyId(Integer familyId)
    {
        return transactionsMapper.selectTransactionsByFamilyId(familyId);
    }

    /**
     * 根据家庭成员ID查询收支记录
     * 
     * @param memberId 家庭成员ID
     * @return 收支记录集合
     */
    @Override
    public List<Transactions> selectTransactionsByMemberId(Integer memberId)
    {
        return transactionsMapper.selectTransactionsByMemberId(memberId);
    }

    /**
     * 根据家庭ID和成员ID查询收支记录
     * 
     * @param familyId 家庭ID
     * @param memberId 成员ID
     * @return 收支记录集合
     */
    @Override
    public List<Transactions> selectTransactionsByFamilyAndMember(Integer familyId, Integer memberId)
    {
        return transactionsMapper.selectTransactionsByFamilyAndMember(familyId, memberId);
    }
}
