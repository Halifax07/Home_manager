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
import com.ruoyi.system.domain.Transactions;
import com.ruoyi.system.domain.FamilyMembers;
import com.ruoyi.system.service.ITransactionsService;
import com.ruoyi.system.service.IFamilyMembersService;
import com.ruoyi.system.service.IFamilyPermissionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 家庭交易记录Controller
 * 
 * @author ruoyi
 * @date 2025-09-07
 */
@RestController
@RequestMapping("/family/transactions")
public class TransactionsController extends BaseController
{
    @Autowired
    private ITransactionsService transactionsService;

    @Autowired
    private IFamilyMembersService familyMembersService;
    
    @Autowired
    private IFamilyPermissionService familyPermissionService;

    /**
     * 检查用户是否为家庭管理员
     */
    private boolean isCurrentUserFamilyAdmin()
    {
        return familyPermissionService.isCurrentUserFamilyAdmin();
    }

    /**
     * 获取当前用户的家庭ID
     */
    private Integer getCurrentUserFamilyId()
    {
        return familyPermissionService.getCurrentUserFamilyId();
    }

    /**
     * 获取当前用户的成员ID
     */
    private Integer getCurrentUserMemberId()
    {
        FamilyMembers currentMember = familyPermissionService.getCurrentFamilyMember();
        return currentMember != null ? currentMember.getId() : null;
    }

    /**
     * 获取当前用户的成员名称
     */
    private String getCurrentUserMemberName()
    {
        FamilyMembers currentMember = familyPermissionService.getCurrentFamilyMember();
        return currentMember != null ? currentMember.getName() : null;
    }

    /**
     * 检查交易记录是否属于当前用户的家庭
     */
    private boolean isTransactionBelongsToUserFamily(Transactions transaction)
    {
        Integer currentFamilyId = getCurrentUserFamilyId();
        return currentFamilyId != null && currentFamilyId.equals(transaction.getFamilyId());
    }

    /**
     * 查询家庭交易记录列表（管理员可以查看家庭所有记录，普通成员只能查看自己的记录）
     */
    @PreAuthorize("@ss.hasPermi('family:transactions:list')")
    @GetMapping("/list")
    public TableDataInfo list(Transactions transactions)
    {
        startPage();
        Integer currentFamilyId = getCurrentUserFamilyId();
        if (currentFamilyId == null) {
            return getDataTable(new java.util.ArrayList<>());
        }
        
        transactions.setFamilyId(currentFamilyId);
        
        // 如果是普通成员，只能查看自己的记录
        if (!isCurrentUserFamilyAdmin()) {
            String currentMemberName = getCurrentUserMemberName();
            if (currentMemberName == null) {
                return getDataTable(new java.util.ArrayList<>());
            }
            transactions.setMemberName(currentMemberName); // 设置成员名称过滤条件
        }
        
        List<Transactions> list = transactionsService.selectTransactionsList(transactions);
        return getDataTable(list);
    }

    /**
     * 导出家庭交易记录列表（管理员导出所有数据，普通成员只导出自己的数据）
     */
    @PreAuthorize("@ss.hasPermi('family:transactions:export')")
    @Log(title = "家庭交易记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Transactions transactions)
    {
        Integer currentFamilyId = getCurrentUserFamilyId();
        if (currentFamilyId == null) {
            return;
        }
        
        transactions.setFamilyId(currentFamilyId);
        
        // 如果是普通成员，只导出自己的记录
        if (!isCurrentUserFamilyAdmin()) {
            String currentMemberName = getCurrentUserMemberName();
            if (currentMemberName == null) {
                return;
            }
            transactions.setMemberName(currentMemberName);
        }
        
        List<Transactions> list = transactionsService.selectTransactionsList(transactions);
        ExcelUtil<Transactions> util = new ExcelUtil<Transactions>(Transactions.class);
        util.exportExcel(response, list, "家庭交易记录数据");
    }

    /**
     * 获取家庭交易记录详细信息（管理员可以查看家庭所有记录，普通成员只能查看自己的记录）
     */
    @PreAuthorize("@ss.hasPermi('family:transactions:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id)
    {
        Transactions transaction = transactionsService.selectTransactionsById(id);
        if (transaction == null) {
            return error("交易记录不存在");
        }
        
        // 检查是否属于当前用户的家庭
        if (!isTransactionBelongsToUserFamily(transaction)) {
            return error("无权查看其他家庭的交易记录");
        }
        
        // 如果是普通成员，只能查看自己的记录
        if (!isCurrentUserFamilyAdmin()) {
            String currentMemberName = getCurrentUserMemberName();
            if (currentMemberName == null || !currentMemberName.equals(transaction.getMemberName())) {
                return error("普通成员只能查看自己的交易记录");
            }
        }
        
        return success(transaction);
    }

    /**
     * 新增家庭交易记录（所有成员都可以添加，但只能添加到自己的家庭）
     */
    @PreAuthorize("@ss.hasPermi('family:transactions:add')")
    @Log(title = "家庭交易记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Transactions transactions)
    {
        // 设置为当前用户的家庭ID和成员ID
        Integer currentFamilyId = getCurrentUserFamilyId();
        Integer currentMemberId = getCurrentUserMemberId();
        
        if (currentFamilyId == null || currentMemberId == null) {
            return error("无法获取当前用户的家庭信息");
        }
        
        transactions.setFamilyId(currentFamilyId);
        transactions.setId(currentMemberId);
        return toAjax(transactionsService.insertTransactions(transactions));
    }

    /**
     * 修改家庭交易记录（管理员可以修改所有记录，普通成员只能修改自己的记录）
     */
    @PreAuthorize("@ss.hasPermi('family:transactions:edit')")
    @Log(title = "家庭交易记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Transactions transactions)
    {
        // 检查交易记录是否属于当前用户的家庭
        Transactions existingTransaction = transactionsService.selectTransactionsById(transactions.getFlowId());
        if (existingTransaction == null) {
            return error("交易记录不存在");
        }
        
        if (!isTransactionBelongsToUserFamily(existingTransaction)) {
            return error("无权修改其他家庭的交易记录");
        }
        
        // 如果是普通成员，只能修改自己的记录
        if (!isCurrentUserFamilyAdmin()) {
            String currentMemberName = getCurrentUserMemberName();
            if (currentMemberName == null || !currentMemberName.equals(existingTransaction.getMemberName())) {
                return error("普通成员只能修改自己的交易记录");
            }
        }
        
        return toAjax(transactionsService.updateTransactions(transactions));
    }

    /**
     * 删除家庭交易记录（管理员可以删除所有记录，普通成员只能删除自己的记录）
     */
    @PreAuthorize("@ss.hasPermi('family:transactions:remove')")
    @Log(title = "家庭交易记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids)
    {
        boolean isAdmin = isCurrentUserFamilyAdmin();
        String currentMemberName = getCurrentUserMemberName();
        
        // 检查所有交易记录是否都属于当前用户的家庭，且普通成员只能删除自己的记录
        for (Integer id : ids) {
            Transactions transaction = transactionsService.selectTransactionsById(id);
            if (transaction == null || !isTransactionBelongsToUserFamily(transaction)) {
                return error("无权删除其他家庭的交易记录");
            }
            
            // 如果是普通成员，只能删除自己的记录
            if (!isAdmin) {
                if (currentMemberName == null || !currentMemberName.equals(transaction.getMemberName())) {
                    return error("普通成员只能删除自己的交易记录");
                }
            }
        }
        
        return toAjax(transactionsService.deleteTransactionsByIds(ids));
    }

    /**
     * 检查当前用户是否有权限修改交易记录
     */
    @GetMapping("/check-edit-permission/{id}")
    public AjaxResult checkEditPermission(@PathVariable("id") Integer id)
    {
        Transactions transaction = transactionsService.selectTransactionsById(id);
        if (transaction == null || !isTransactionBelongsToUserFamily(transaction)) {
            return success(false);
        }
        
        // 管理员可以修改所有记录，普通成员只能修改自己的记录
        boolean canEdit = isCurrentUserFamilyAdmin();
        if (!canEdit) {
            String currentMemberName = getCurrentUserMemberName();
            canEdit = currentMemberName != null && currentMemberName.equals(transaction.getMemberName());
        }
        
        return success(canEdit);
    }

    /**
     * 根据家庭ID查询交易记录（只能查询自己的家庭）
     */
    @PreAuthorize("@ss.hasPermi('family:transactions:list')")
    @GetMapping("/family/{familyId}")
    public AjaxResult getTransactionsByFamilyId(@PathVariable("familyId") Integer familyId)
    {
        // 检查是否为当前用户的家庭ID
        Integer currentFamilyId = getCurrentUserFamilyId();
        if (currentFamilyId == null || !currentFamilyId.equals(familyId)) {
            return error("无权查看其他家庭的交易记录");
        }
        
        List<Transactions> list = transactionsService.selectTransactionsByFamilyId(familyId);
        return success(list);
    }

    /**
     * 根据成员ID查询交易记录（管理员可以查询所有家庭成员记录，普通成员只能查询自己的记录）
     */
    @PreAuthorize("@ss.hasPermi('family:transactions:list')")
    @GetMapping("/member/{memberId}")
    public AjaxResult getTransactionsByMemberId(@PathVariable("memberId") Integer memberId)
    {
        // 检查该成员是否属于当前用户的家庭
        FamilyMembers member = familyMembersService.selectFamilyMembersById(memberId);
        if (member == null) {
            return error("成员不存在");
        }
        
        Integer currentFamilyId = getCurrentUserFamilyId();
        if (currentFamilyId == null || !currentFamilyId.equals(member.getFamilyId())) {
            return error("无权查看其他家庭成员的交易记录");
        }
        
        // 如果是普通成员，只能查询自己的记录
        if (!isCurrentUserFamilyAdmin()) {
            Integer currentMemberId = getCurrentUserMemberId();
            if (currentMemberId == null || !currentMemberId.equals(memberId)) {
                return error("普通成员只能查看自己的交易记录");
            }
        }
        
        List<Transactions> list = transactionsService.selectTransactionsByMemberId(memberId);
        return success(list);
    }

    /**
     * 根据交易类型查询记录
     */
    @PreAuthorize("@ss.hasPermi('family:transactions:list')")
    @GetMapping("/type/{txnType}")
    public AjaxResult getTransactionsByType(@PathVariable("txnType") String txnType)
    {
        Transactions transactions = new Transactions();
        transactions.setTxnType(txnType);
        List<Transactions> list = transactionsService.selectTransactionsList(transactions);
        return success(list);
    }
}
