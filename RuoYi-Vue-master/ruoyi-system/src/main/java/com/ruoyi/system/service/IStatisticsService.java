package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

/**
 * 收支统计Service接口
 * 
 * @author ruoyi
 * @date 2025-01-20
 */
public interface IStatisticsService 
{
    /**
     * 获取统计概览数据
     * 
     * @param familyId 家庭ID
     * @return 统计概览数据
     */
    public Map<String, Object> getStatisticsOverview(Integer familyId);

    /**
     * 获取统计概览数据（按成员过滤）
     * 
     * @param familyId 家庭ID
     * @param memberId 成员ID，为null时统计整个家庭
     * @return 统计概览数据
     */
    public Map<String, Object> getStatisticsOverview(Integer familyId, Integer memberId);

    /**
     * 获取分类统计数据
     * 
     * @param familyId 家庭ID
     * @param txnType 交易类型（收入/支出）
     * @return 分类统计数据
     */
    public List<Map<String, Object>> getCategoryStatistics(Integer familyId, String txnType);

    /**
     * 获取分类统计数据（按成员过滤）
     * 
     * @param familyId 家庭ID
     * @param txnType 交易类型（收入/支出）
     * @param memberId 成员ID，为null时统计整个家庭
     * @return 分类统计数据
     */
    public List<Map<String, Object>> getCategoryStatistics(Integer familyId, String txnType, Integer memberId);

    /**
     * 获取月度收支趋势
     * 
     * @param familyId 家庭ID
     * @return 月度趋势数据
     */
    public List<Map<String, Object>> getMonthlyTrend(Integer familyId);

    /**
     * 获取月度收支趋势（按成员过滤）
     * 
     * @param familyId 家庭ID
     * @param memberId 成员ID，为null时统计整个家庭
     * @return 月度趋势数据
     */
    public List<Map<String, Object>> getMonthlyTrend(Integer familyId, Integer memberId);
}
