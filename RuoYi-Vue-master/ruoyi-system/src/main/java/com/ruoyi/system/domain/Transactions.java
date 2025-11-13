package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 收支记录对象 transactions
 * 
 * @author ruoyi
 * @date 2025-09-07
 */
public class Transactions extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 交易ID */
    private Integer flowId;

    /** 家庭ID */
    @Excel(name = "家庭ID")
    private Integer familyId;

    /** 交易金额（建议恒为正数） */
    @Excel(name = "交易金额")
    private BigDecimal amount;

    /** 交易类型 */
    @Excel(name = "交易类型")
    private String txnType;

    /** 分类（可为空） */
    @Excel(name = "分类")
    private String category;

    /** 创建该记录的家庭成员ID */
    private Integer id;

    /** 交易日期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "交易时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date transactionDate;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    // 关联查询字段
    private String memberName;
    private String familyName;

    public void setFlowId(Integer flowId) 
    {
        this.flowId = flowId;
    }

    public Integer getFlowId() 
    {
        return flowId;
    }
    
    public void setFamilyId(Integer familyId) 
    {
        this.familyId = familyId;
    }

    public Integer getFamilyId() 
    {
        return familyId;
    }
    
    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }
    
    public void setTxnType(String txnType) 
    {
        this.txnType = txnType;
    }

    public String getTxnType() 
    {
        return txnType;
    }
    
    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }
    
    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    
    public void setTransactionDate(Date transactionDate) 
    {
        this.transactionDate = transactionDate;
    }

    public Date getTransactionDate() 
    {
        return transactionDate;
    }
    
    public void setNote(String note) 
    {
        this.note = note;
    }

    public String getNote() 
    {
        return note;
    }

    public String getMemberName() 
    {
        return memberName;
    }

    public void setMemberName(String memberName) 
    {
        this.memberName = memberName;
    }

    public String getFamilyName() 
    {
        return familyName;
    }

    public void setFamilyName(String familyName) 
    {
        this.familyName = familyName;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "flowId=" + flowId +
                ", familyId=" + familyId +
                ", amount=" + amount +
                ", txnType='" + txnType + '\'' +
                ", category='" + category + '\'' +
                ", id=" + id +
                ", transactionDate=" + transactionDate +
                ", note='" + note + '\'' +
                ", memberName='" + memberName + '\'' +
                ", familyName='" + familyName + '\'' +
                '}';
    }
}
