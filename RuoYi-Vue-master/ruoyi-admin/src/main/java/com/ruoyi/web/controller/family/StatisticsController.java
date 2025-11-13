package com.ruoyi.web.controller.family;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.FamilyMembers;
import com.ruoyi.system.service.IFamilyMembersService;
import com.ruoyi.system.service.IFamilyPermissionService;
import com.ruoyi.system.service.IStatisticsService;

/**
 * 收支统计Controller
 * 
 * @author ruoyi
 * @date 2025-01-20
 */
@RestController
@RequestMapping("/family/statistics")
public class StatisticsController extends BaseController
{
    @Autowired
    private IStatisticsService statisticsService;

    @Autowired
    private IFamilyMembersService familyMembersService;

    @Autowired
    private IFamilyPermissionService familyPermissionService;

    /**
     * 获取当前用户的家庭成员信息
     */
    private FamilyMembers getCurrentUserFamilyMember() {
        String username = SecurityUtils.getUsername();
        FamilyMembers member = familyMembersService.selectMemberByAccount(username);
        return member;
    }

    /**
     * 检查当前用户是否为家庭管理员
     */
    private boolean isCurrentUserFamilyAdmin() {
        return familyPermissionService.isCurrentUserFamilyAdmin();
    }

    /**
     * 获取收支统计概览（管理员查看家庭全部，普通成员只查看自己的）
     */
    @PreAuthorize("@ss.hasPermi('family:statistics:list')")
    @GetMapping("/overview")
    @Log(title = "收支统计概览", businessType = BusinessType.OTHER)
    public AjaxResult getOverview()
    {
        FamilyMembers currentMember = getCurrentUserFamilyMember();
        if (currentMember == null) {
            return error("用户未绑定家庭成员信息");
        }

        try {
            Map<String, Object> overview;
            // 如果是管理员，查看家庭全部数据；如果是普通成员，只查看自己的数据
            if (isCurrentUserFamilyAdmin()) {
                overview = statisticsService.getStatisticsOverview(currentMember.getFamilyId(), null);
            } else {
                overview = statisticsService.getStatisticsOverview(currentMember.getFamilyId(), currentMember.getId());
            }
            return success(overview);
        } catch (Exception e) {
            logger.error("获取收支统计概览失败", e);
            return error("获取统计数据失败");
        }
    }

    /**
     * 获取收入分类统计（管理员查看家庭全部，普通成员只查看自己的）
     */
    @PreAuthorize("@ss.hasPermi('family:statistics:list')")
    @GetMapping("/income-category")
    @Log(title = "收入分类统计", businessType = BusinessType.OTHER)
    public AjaxResult getIncomeCategoryStatistics()
    {
        FamilyMembers currentMember = getCurrentUserFamilyMember();
        if (currentMember == null) {
            return error("用户未绑定家庭成员信息");
        }

        try {
            List<Map<String, Object>> statistics;
            // 如果是管理员，查看家庭全部数据；如果是普通成员，只查看自己的数据
            if (isCurrentUserFamilyAdmin()) {
                statistics = statisticsService.getCategoryStatistics(currentMember.getFamilyId(), "收入", null);
            } else {
                statistics = statisticsService.getCategoryStatistics(currentMember.getFamilyId(), "收入", currentMember.getId());
            }
            return success(statistics);
        } catch (Exception e) {
            logger.error("获取收入分类统计失败", e);
            return error("获取统计数据失败");
        }
    }

    /**
     * 获取支出分类统计（管理员查看家庭全部，普通成员只查看自己的）
     */
    @PreAuthorize("@ss.hasPermi('family:statistics:list')")
    @GetMapping("/expense-category")
    @Log(title = "支出分类统计", businessType = BusinessType.OTHER)
    public AjaxResult getExpenseCategoryStatistics()
    {
        FamilyMembers currentMember = getCurrentUserFamilyMember();
        if (currentMember == null) {
            return error("用户未绑定家庭成员信息");
        }

        try {
            List<Map<String, Object>> statistics;
            // 如果是管理员，查看家庭全部数据；如果是普通成员，只查看自己的数据
            if (isCurrentUserFamilyAdmin()) {
                statistics = statisticsService.getCategoryStatistics(currentMember.getFamilyId(), "支出", null);
            } else {
                statistics = statisticsService.getCategoryStatistics(currentMember.getFamilyId(), "支出", currentMember.getId());
            }
            return success(statistics);
        } catch (Exception e) {
            logger.error("获取支出分类统计失败", e);
            return error("获取统计数据失败");
        }
    }

    /**
     * 获取月度收支趋势（管理员查看家庭全部，普通成员只查看自己的）
     */
    @PreAuthorize("@ss.hasPermi('family:statistics:list')")
    @GetMapping("/monthly-trend")
    @Log(title = "月度收支趋势", businessType = BusinessType.OTHER)
    public AjaxResult getMonthlyTrend()
    {
        FamilyMembers currentMember = getCurrentUserFamilyMember();
        if (currentMember == null) {
            return error("用户未绑定家庭成员信息");
        }

        try {
            List<Map<String, Object>> trend;
            // 如果是管理员，查看家庭全部数据；如果是普通成员，只查看自己的数据
            if (isCurrentUserFamilyAdmin()) {
                trend = statisticsService.getMonthlyTrend(currentMember.getFamilyId(), null);
            } else {
                trend = statisticsService.getMonthlyTrend(currentMember.getFamilyId(), currentMember.getId());
            }
            return success(trend);
        } catch (Exception e) {
            logger.error("获取月度收支趋势失败", e);
            return error("获取统计数据失败");
        }
    }
}
