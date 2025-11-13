package com.ruoyi.system.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.Transactions;
import org.apache.ibatis.annotations.Param;

/**
 * 收支记录Mapper接口
 * 
 * @author ruoyi
 * @date 2025-09-07
 */
public interface TransactionsMapper 
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
     * 删除收支记录
     * 
     * @param flowId 收支记录主键
     * @return 结果
     */
    public int deleteTransactionsById(Integer flowId);

    /**
     * 批量删除收支记录
     * 
     * @param flowIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTransactionsByIds(Integer[] flowIds);

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

    /**
     * 根据交易类型获取家庭总金额
     * 
     * @param familyId 家庭ID
     * @param txnType 交易类型
     * @return 总金额
     */
    public BigDecimal getTotalAmountByType(@Param("familyId") Integer familyId, @Param("txnType") String txnType);

    /**
     * 根据交易类型获取成员总金额
     * 
     * @param familyId 家庭ID
     * @param memberId 成员ID
     * @param txnType 交易类型
     * @return 总金额
     */
    public BigDecimal getTotalAmountByTypeAndMember(@Param("familyId") Integer familyId, @Param("memberId") Integer memberId, @Param("txnType") String txnType);

    /**
     * 获取家庭交易记录总数
     * 
     * @param familyId 家庭ID
     * @return 交易记录总数
     */
    public Long getTransactionCount(@Param("familyId") Integer familyId);

    /**
     * 获取成员交易记录总数
     * 
     * @param familyId 家庭ID
     * @param memberId 成员ID
     * @return 交易记录总数
     */
    public Long getTransactionCountByMember(@Param("familyId") Integer familyId, @Param("memberId") Integer memberId);

    /**
     * 获取分类统计数据
     * 
     * @param familyId 家庭ID
     * @param txnType 交易类型
     * @return 分类统计数据
     */
    public List<Map<String, Object>> getCategoryStatistics(@Param("familyId") Integer familyId, @Param("txnType") String txnType);

    /**
     * 获取成员分类统计数据
     * 
     * @param familyId 家庭ID
     * @param memberId 成员ID
     * @param txnType 交易类型
     * @return 分类统计数据
     */
    public List<Map<String, Object>> getCategoryStatisticsByMember(@Param("familyId") Integer familyId, @Param("memberId") Integer memberId, @Param("txnType") String txnType);

    /**
     * 获取月度收支趋势
     * 
     * @param familyId 家庭ID
     * @return 月度趋势数据
     */
    public List<Map<String, Object>> getMonthlyTrend(@Param("familyId") Integer familyId);

    /**
     * 获取成员月度收支趋势
     * 
     * @param familyId 家庭ID
     * @param memberId 成员ID
     * @return 月度趋势数据
     */
    public List<Map<String, Object>> getMonthlyTrendByMember(@Param("familyId") Integer familyId, @Param("memberId") Integer memberId);
}
