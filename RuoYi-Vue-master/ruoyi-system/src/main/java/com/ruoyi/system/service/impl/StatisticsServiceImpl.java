package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.system.mapper.TransactionsMapper;
import com.ruoyi.system.service.IStatisticsService;

/**
 * 收支统计Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-01-20
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService 
{
    @Autowired
    private TransactionsMapper transactionsMapper;

    /**
     * 获取统计概览数据
     * 
     * @param familyId 家庭ID
     * @return 统计概览数据
     */
    @Override
    public Map<String, Object> getStatisticsOverview(Integer familyId)
    {
        Map<String, Object> overview = new HashMap<>();
        
        // 获取总收入
        BigDecimal totalIncome = transactionsMapper.getTotalAmountByType(familyId, "收入");
        overview.put("totalIncome", totalIncome != null ? totalIncome : BigDecimal.ZERO);
        
        // 获取总支出
        BigDecimal totalExpense = transactionsMapper.getTotalAmountByType(familyId, "支出");
        overview.put("totalExpense", totalExpense != null ? totalExpense : BigDecimal.ZERO);
        
        // 计算结余
        BigDecimal balance = ((BigDecimal) overview.get("totalIncome")).subtract((BigDecimal) overview.get("totalExpense"));
        overview.put("balance", balance);
        
        // 获取交易记录总数
        Long totalTransactions = transactionsMapper.getTransactionCount(familyId);
        overview.put("totalTransactions", totalTransactions != null ? totalTransactions : 0L);
        
        return overview;
    }

    /**
     * 获取分类统计数据
     * 
     * @param familyId 家庭ID
     * @param txnType 交易类型（收入/支出）
     * @return 分类统计数据
     */
    @Override
    public List<Map<String, Object>> getCategoryStatistics(Integer familyId, String txnType)
    {
        return transactionsMapper.getCategoryStatistics(familyId, txnType);
    }

    /**
     * 获取月度收支趋势
     * 
     * @param familyId 家庭ID
     * @return 月度趋势数据
     */
    @Override
    public List<Map<String, Object>> getMonthlyTrend(Integer familyId)
    {
        return transactionsMapper.getMonthlyTrend(familyId);
    }

    /**
     * 获取统计概览数据（按成员过滤）
     * 
     * @param familyId 家庭ID
     * @param memberId 成员ID，为null时统计整个家庭
     * @return 统计概览数据
     */
    @Override
    public Map<String, Object> getStatisticsOverview(Integer familyId, Integer memberId)
    {
        Map<String, Object> overview = new HashMap<>();
        
        if (memberId == null) {
            // 如果memberId为null，调用原有的家庭统计方法
            return getStatisticsOverview(familyId);
        }
        
        // 获取成员总收入
        BigDecimal totalIncome = transactionsMapper.getTotalAmountByTypeAndMember(familyId, memberId, "收入");
        overview.put("totalIncome", totalIncome != null ? totalIncome : BigDecimal.ZERO);
        
        // 获取成员总支出
        BigDecimal totalExpense = transactionsMapper.getTotalAmountByTypeAndMember(familyId, memberId, "支出");
        overview.put("totalExpense", totalExpense != null ? totalExpense : BigDecimal.ZERO);
        
        // 计算结余
        BigDecimal balance = ((BigDecimal) overview.get("totalIncome")).subtract((BigDecimal) overview.get("totalExpense"));
        overview.put("balance", balance);
        
        // 获取成员交易记录总数
        Long totalTransactions = transactionsMapper.getTransactionCountByMember(familyId, memberId);
        overview.put("totalTransactions", totalTransactions != null ? totalTransactions : 0L);
        
        return overview;
    }

    /**
     * 获取分类统计数据（按成员过滤）
     * 
     * @param familyId 家庭ID
     * @param txnType 交易类型（收入/支出）
     * @param memberId 成员ID，为null时统计整个家庭
     * @return 分类统计数据
     */
    @Override
    public List<Map<String, Object>> getCategoryStatistics(Integer familyId, String txnType, Integer memberId)
    {
        if (memberId == null) {
            // 如果memberId为null，调用原有的家庭统计方法
            return getCategoryStatistics(familyId, txnType);
        }
        
        return transactionsMapper.getCategoryStatisticsByMember(familyId, memberId, txnType);
    }

    /**
     * 获取月度收支趋势（按成员过滤）
     * 
     * @param familyId 家庭ID
     * @param memberId 成员ID，为null时统计整个家庭
     * @return 月度趋势数据
     */
    @Override
    public List<Map<String, Object>> getMonthlyTrend(Integer familyId, Integer memberId)
    {
        if (memberId == null) {
            // 如果memberId为null，调用原有的家庭统计方法
            return getMonthlyTrend(familyId);
        }
        
        return transactionsMapper.getMonthlyTrendByMember(familyId, memberId);
    }
}
