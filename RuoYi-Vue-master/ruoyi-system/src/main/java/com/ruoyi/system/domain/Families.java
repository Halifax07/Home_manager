package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 家庭信息对象 families
 * 
 * @author ruoyi
 * @date 2025-09-07
 */
public class Families extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 家庭ID */
    private Integer familieId;

    /** 家庭名称（唯一） */
    @Excel(name = "家庭名称")
    private String familyName;

    public void setFamilieId(Integer familieId) 
    {
        this.familieId = familieId;
    }

    public Integer getFamilieId() 
    {
        return familieId;
    }
    
    public void setFamilyName(String familyName) 
    {
        this.familyName = familyName;
    }

    public String getFamilyName() 
    {
        return familyName;
    }

    @Override
    public String toString() {
        return "Families{" +
                "familieId=" + familieId +
                ", familyName='" + familyName + '\'' +
                '}';
    }
}