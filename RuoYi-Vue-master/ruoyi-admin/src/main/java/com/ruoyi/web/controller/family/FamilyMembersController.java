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
import com.ruoyi.system.domain.FamilyMembers;
import com.ruoyi.system.service.IFamilyMembersService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 家庭成员Controller
 * 
 * @author ruoyi
 * @date 2025-09-07
 */
@RestController
@RequestMapping("/family/members")
public class FamilyMembersController extends BaseController
{
    @Autowired
    private IFamilyMembersService familyMembersService;

    /**
     * 获取当前用户的家庭ID
     */
    private Integer getCurrentUserFamilyId()
    {
        String username = SecurityUtils.getUsername();
        FamilyMembers currentMember = familyMembersService.selectMemberByAccount(username);
        return currentMember != null ? currentMember.getFamilyId() : null;
    }

    /**
     * 检查是否为家庭管理员
     */
    private boolean isCurrentUserFamilyAdmin()
    {
        String username = SecurityUtils.getUsername();
        FamilyMembers currentMember = familyMembersService.selectMemberByAccount(username);
        if (currentMember == null) {
            return false;
        }
        return "管理员".equals(currentMember.getRole());
    }

    /**
     * 查询家庭成员列表（只能查看自己家庭的成员）
     */
    @PreAuthorize("@ss.hasPermi('family:members:list')")
    @GetMapping("/list")
    public TableDataInfo list(FamilyMembers familyMembers)
    {
        startPage();
        // 只查询当前用户家庭的成员
        Integer currentFamilyId = getCurrentUserFamilyId();
        if (currentFamilyId == null) {
            return getDataTable(new java.util.ArrayList<>());
        }
        familyMembers.setFamilyId(currentFamilyId);
        List<FamilyMembers> list = familyMembersService.selectFamilyMembersList(familyMembers);
        return getDataTable(list);
    }

    /**
     * 导出家庭成员列表
     */
    @PreAuthorize("@ss.hasPermi('family:members:export')")
    @Log(title = "家庭成员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FamilyMembers familyMembers)
    {
        List<FamilyMembers> list = familyMembersService.selectFamilyMembersList(familyMembers);
        ExcelUtil<FamilyMembers> util = new ExcelUtil<FamilyMembers>(FamilyMembers.class);
        util.exportExcel(response, list, "家庭成员数据");
    }

    /**
     * 获取家庭成员详细信息
     */
    @PreAuthorize("@ss.hasPermi('family:members:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        return success(familyMembersService.selectFamilyMembersById(id));
    }

    /**
     * 检查当前用户是否为家庭管理员
     */
    @GetMapping("/check-admin")
    public AjaxResult checkAdmin()
    {
        boolean isAdmin = isCurrentUserFamilyAdmin();
        return success(isAdmin);
    }

    /**
     * 新增家庭成员
     */
    @PreAuthorize("@ss.hasPermi('family:members:add')")
    @Log(title = "家庭成员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FamilyMembers familyMembers)
    {
        // 检查是否为家庭管理员
        if (!isCurrentUserFamilyAdmin()) {
            return error("只有家庭管理员才能添加家庭成员");
        }
        
        // 获取当前用户的家庭ID
        Integer currentFamilyId = getCurrentUserFamilyId();
        if (currentFamilyId == null) {
            return error("无法获取当前用户的家庭信息");
        }
        
        // 设置新成员的家庭ID
        familyMembers.setFamilyId(currentFamilyId);
        
        if (!familyMembersService.checkAccountUnique(familyMembers))
        {
            return error("新增家庭成员'" + familyMembers.getName() + "'失败，登录账号已存在");
        }
        return toAjax(familyMembersService.insertFamilyMembers(familyMembers));
    }

    /**
     * 修改家庭成员
     */
    @PreAuthorize("@ss.hasPermi('family:members:edit')")
    @Log(title = "家庭成员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FamilyMembers familyMembers)
    {
        // 获取当前用户信息
        String currentUsername = SecurityUtils.getUsername();
        FamilyMembers currentMember = familyMembersService.selectMemberByAccount(currentUsername);
        
        if (currentMember == null) {
            return error("无法获取当前用户信息");
        }
        
        // 检查是否修改自己的信息
        boolean isEditingSelf = currentMember.getId().equals(familyMembers.getId());
        
        // 如果不是修改自己的信息，需要是家庭管理员
        if (!isEditingSelf && !isCurrentUserFamilyAdmin()) {
            return error("只有家庭管理员才能修改其他成员的信息");
        }
        
        // 检查是否属于同一个家庭
        FamilyMembers targetMember = familyMembersService.selectFamilyMembersById(familyMembers.getId());
        if (targetMember == null) {
            return error("目标成员不存在");
        }
        
        if (!currentMember.getFamilyId().equals(targetMember.getFamilyId())) {
            return error("无权修改其他家庭成员的信息");
        }
        
        // 如果修改的是角色字段，只有家庭管理员才能修改
        if (!isCurrentUserFamilyAdmin() && familyMembers.getRole() != null && 
            !familyMembers.getRole().equals(targetMember.getRole())) {
            return error("只有家庭管理员才能修改成员角色");
        }
        
        // 如果普通用户修改自己信息，不允许修改某些敏感字段
        if (isEditingSelf && !"管理员".equals(currentMember.getRole())) {
            // 普通用户只能修改自己的基本信息，不能修改角色、账号等
            familyMembers.setRole(null); // 不允许修改角色
            familyMembers.setAccount(null); // 不允许修改账号
            familyMembers.setFamilyId(null); // 不允许修改家庭ID
        }
        
        // 检查账号唯一性
        if (familyMembers.getAccount() != null && !familyMembersService.checkAccountUnique(familyMembers))
        {
            return error("修改家庭成员'" + familyMembers.getName() + "'失败，登录账号已存在");
        }
        
        return toAjax(familyMembersService.updateFamilyMembers(familyMembers));
    }

    /**
     * 删除家庭成员
     */
    @PreAuthorize("@ss.hasPermi('family:members:remove')")
    @Log(title = "家庭成员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        // 检查是否为家庭管理员
        if (!isCurrentUserFamilyAdmin()) {
            return error("只有家庭管理员才能删除家庭成员");
        }
        
        // 检查是否试图删除自己
        String currentUsername = SecurityUtils.getUsername();
        FamilyMembers currentMember = familyMembersService.selectMemberByAccount(currentUsername);
        
        for (Integer id : ids) {
            if (currentMember != null && currentMember.getId().equals(id)) {
                return error("不能删除自己的账户");
            }
            
            // 检查该成员是否有关联的交易记录
            FamilyMembers member = familyMembersService.selectFamilyMembersById(id);
            if (member == null) {
                return error("家庭成员不存在");
            }
            
            // 检查是否属于同一个家庭
            if (currentMember == null || !currentMember.getFamilyId().equals(member.getFamilyId())) {
                return error("无权删除其他家庭的成员");
            }
        }
        
        try {
            return toAjax(familyMembersService.deleteFamilyMembersByIds(ids));
        } catch (Exception e) {
            if (e.getMessage().contains("foreign key constraint fails")) {
                return error("该成员有关联的交易记录，无法直接删除。请先删除相关交易记录。");
            }
            return error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据家庭ID查询成员列表
     */
    @PreAuthorize("@ss.hasPermi('family:members:list')")
    @GetMapping("/family/{familyId}")
    public AjaxResult getMembersByFamilyId(@PathVariable("familyId") Integer familyId)
    {
        List<FamilyMembers> list = familyMembersService.selectMembersByFamilyId(familyId);
        return success(list);
    }

    /**
     * 获取当前用户的个人信息
     */
    @GetMapping("/personal")
    public AjaxResult getPersonalInfo()
    {
        String username = SecurityUtils.getUsername();
        FamilyMembers personalInfo = familyMembersService.selectMemberByAccount(username);
        if (personalInfo == null) {
            return error("未找到个人信息");
        }
        
        // 获取家庭名称
        if (personalInfo.getFamilyId() != null) {
            // 通过查询家庭成员列表来获取家庭名称
            FamilyMembers memberWithFamily = new FamilyMembers();
            memberWithFamily.setFamilyId(personalInfo.getFamilyId());
            List<FamilyMembers> memberList = familyMembersService.selectFamilyMembersList(memberWithFamily);
            if (!memberList.isEmpty() && memberList.get(0).getFamilyName() != null) {
                personalInfo.setFamilyName(memberList.get(0).getFamilyName());
            }
        }
        
        return success(personalInfo);
    }

    /**
     * 更新当前用户的个人信息
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/personal")
    public AjaxResult updatePersonalInfo(@RequestBody FamilyMembers familyMembers)
    {
        String username = SecurityUtils.getUsername();
        FamilyMembers currentMember = familyMembersService.selectMemberByAccount(username);
        if (currentMember == null) {
            return error("未找到个人信息");
        }
        
        // 只允许修改特定字段
        currentMember.setName(familyMembers.getName());
        currentMember.setGender(familyMembers.getGender());
        currentMember.setKinship(familyMembers.getKinship());
        currentMember.setOccupation(familyMembers.getOccupation());
        
        return toAjax(familyMembersService.updateFamilyMembers(currentMember));
    }

    /**
     * 检查当前用户的操作权限
     */
    @GetMapping("/check-permissions/{id}")
    public AjaxResult checkPermissions(@PathVariable("id") Integer id)
    {
        String currentUsername = SecurityUtils.getUsername();
        FamilyMembers currentMember = familyMembersService.selectMemberByAccount(currentUsername);
        
        if (currentMember == null) {
            return error("无法获取当前用户信息");
        }

        boolean isAdmin = isCurrentUserFamilyAdmin();
        boolean isEditingSelf = currentMember.getId().equals(id);
        boolean canEdit = isAdmin || isEditingSelf;
        boolean canDelete = isAdmin && !isEditingSelf;

        java.util.Map<String, Object> permissions = new java.util.HashMap<>();
        permissions.put("canEdit", canEdit);
        permissions.put("canDelete", canDelete);
        permissions.put("isAdmin", isAdmin);
        permissions.put("isEditingSelf", isEditingSelf);

        return success(permissions);
    }
}
