package com.ruoyi.web.controller.family;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.FamilyRegister;
import com.ruoyi.system.service.IFamilyRegisterService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 家庭注册申请Controller
 * 
 * @author ruoyi
 * @date 2025-09-09
 */
@RestController
@RequestMapping("/family/register")
public class FamilyRegisterController extends BaseController
{
    @Autowired
    private IFamilyRegisterService familyRegisterService;

    /**
     * 查询家庭注册申请列表（仅管理员可访问）
     */
    @PreAuthorize("@ss.hasPermi('family:register:list')")
    @GetMapping("/list")
    public TableDataInfo list(FamilyRegister familyRegister)
    {
        startPage();
        List<FamilyRegister> list = familyRegisterService.selectFamilyRegisterList(familyRegister);
        return getDataTable(list);
    }

    /**
     * 导出家庭注册申请列表
     */
    @PreAuthorize("@ss.hasPermi('family:register:export')")
    @Log(title = "家庭注册申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FamilyRegister familyRegister)
    {
        List<FamilyRegister> list = familyRegisterService.selectFamilyRegisterList(familyRegister);
        ExcelUtil<FamilyRegister> util = new ExcelUtil<FamilyRegister>(FamilyRegister.class);
        util.exportExcel(response, list, "家庭注册申请数据");
    }

    /**
     * 获取家庭注册申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('family:register:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(familyRegisterService.selectFamilyRegisterById(id));
    }

    /**
     * 新增家庭注册申请（此接口一般不对外开放，使用/register接口）
     */
    @PreAuthorize("@ss.hasPermi('family:register:add')")
    @Log(title = "家庭注册申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FamilyRegister familyRegister)
    {
        return toAjax(familyRegisterService.insertFamilyRegister(familyRegister));
    }

    /**
     * 修改家庭注册申请
     */
    @PreAuthorize("@ss.hasPermi('family:register:edit')")
    @Log(title = "家庭注册申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FamilyRegister familyRegister)
    {
        return toAjax(familyRegisterService.updateFamilyRegister(familyRegister));
    }

    /**
     * 删除家庭注册申请
     */
    @PreAuthorize("@ss.hasPermi('family:register:remove')")
    @Log(title = "家庭注册申请", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        return toAjax(familyRegisterService.deleteFamilyRegisterByIds(ids));
    }

    /**
     * 审批注册申请
     * @param id 申请ID
     * @param status 审批状态（1-通过，2-拒绝）
     * @param remark 审批备注
     */
    @PreAuthorize("@ss.hasPermi('family:register:approve')")
    @Log(title = "审批注册申请", businessType = BusinessType.UPDATE)
    @PostMapping("/approve/{id}")
    public AjaxResult approve(@PathVariable("id") Integer id, String status, String remark)
    {
        try {
            String approveBy = SecurityUtils.getUsername();
            int result = familyRegisterService.approveRegister(id, status, approveBy, remark);
            if (result > 0) {
                return success("审批成功");
            } else {
                return error("审批失败");
            }
        } catch (Exception e) {
            logger.error("审批失败", e);
            return error("审批失败：" + e.getMessage());
        }
    }

    /**
     * 查询待审批的注册申请
     */
    @PreAuthorize("@ss.hasPermi('family:register:list')")
    @GetMapping("/pending")
    public AjaxResult getPendingRegisters()
    {
        FamilyRegister condition = new FamilyRegister();
        condition.setStatus("0"); // 0=待审批
        List<FamilyRegister> list = familyRegisterService.selectFamilyRegisterList(condition);
        return success(list);
    }
}
