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
import com.ruoyi.system.domain.Families;
import com.ruoyi.system.service.IFamiliesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 家庭信息Controller
 * 
 * @author ruoyi
 * @date 2025-09-07
 */
@RestController
@RequestMapping("/family/families")
public class FamiliesController extends BaseController
{
    @Autowired
    private IFamiliesService familiesService;

    /**
     * 查询家庭信息列表
     */
    @PreAuthorize("@ss.hasPermi('family:families:list')")
    @GetMapping("/list")
    public TableDataInfo list(Families families)
    {
        startPage();
        List<Families> list = familiesService.selectFamiliesList(families);
        return getDataTable(list);
    }

    /**
     * 导出家庭信息列表
     */
    @PreAuthorize("@ss.hasPermi('family:families:export')")
    @Log(title = "家庭信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Families families)
    {
        List<Families> list = familiesService.selectFamiliesList(families);
        ExcelUtil<Families> util = new ExcelUtil<Families>(Families.class);
        util.exportExcel(response, list, "家庭信息数据");
    }

    /**
     * 获取家庭信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('family:families:query')")
    @GetMapping(value = "/{familieId}")
    public AjaxResult getInfo(@PathVariable("familieId") Integer familieId)
    {
        return success(familiesService.selectFamiliesById(familieId));
    }

    /**
     * 新增家庭信息
     */
    @PreAuthorize("@ss.hasPermi('family:families:add')")
    @Log(title = "家庭信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Families families)
    {
        if (!familiesService.checkFamilyNameUnique(families))
        {
            return error("新增家庭'" + families.getFamilyName() + "'失败，家庭名称已存在");
        }
        return toAjax(familiesService.insertFamilies(families));
    }

    /**
     * 修改家庭信息
     */
    @PreAuthorize("@ss.hasPermi('family:families:edit')")
    @Log(title = "家庭信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Families families)
    {
        if (!familiesService.checkFamilyNameUnique(families))
        {
            return error("修改家庭'" + families.getFamilyName() + "'失败，家庭名称已存在");
        }
        return toAjax(familiesService.updateFamilies(families));
    }

    /**
     * 删除家庭信息
     */
    @PreAuthorize("@ss.hasPermi('family:families:remove')")
    @Log(title = "家庭信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{familieIds}")
    public AjaxResult remove(@PathVariable Integer[] familieIds)
    {
        return toAjax(familiesService.deleteFamiliesByIds(familieIds));
    }
}
