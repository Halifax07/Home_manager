package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 家庭注册申请对象 family_register
 * 
 * @author ruoyi
 * @date 2025-09-09
 */
public class FamilyRegister extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 注册ID */
    private Integer id;

    /** 注册账号 */
    @Excel(name = "注册账号")
    private String account;

    /** 密码哈希 */
    private String passwordHash;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String name;

    /** 性别 */
    @Excel(name = "性别", readConverterExp = "男=男,女=女")
    private String gender;

    /** 家庭关系 */
    @Excel(name = "家庭关系")
    private String kinship;

    /** 职业 */
    @Excel(name = "职业")
    private String occupation;

    /** 用户类型 */
    @Excel(name = "用户类型", readConverterExp = "admin=家庭管理员,member=普通成员")
    private String userType;

    /** 申请加入的家庭名称 */
    @Excel(name = "申请家庭")
    private String familyName;

    /** 审批状态 */
    @Excel(name = "审批状态", readConverterExp = "0=待审批,1=已通过,2=已拒绝")
    private String status;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date approveTime;

    /** 审批人 */
    @Excel(name = "审批人")
    private String approveBy;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    
    public void setAccount(String account) 
    {
        this.account = account;
    }

    public String getAccount() 
    {
        return account;
    }
    
    public void setPasswordHash(String passwordHash) 
    {
        this.passwordHash = passwordHash;
    }

    public String getPasswordHash() 
    {
        return passwordHash;
    }
    
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    
    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }
    
    public void setKinship(String kinship) 
    {
        this.kinship = kinship;
    }

    public String getKinship() 
    {
        return kinship;
    }
    
    public void setOccupation(String occupation) 
    {
        this.occupation = occupation;
    }

    public String getOccupation() 
    {
        return occupation;
    }
    
    public void setUserType(String userType) 
    {
        this.userType = userType;
    }

    public String getUserType() 
    {
        return userType;
    }
    
    public void setFamilyName(String familyName) 
    {
        this.familyName = familyName;
    }

    public String getFamilyName() 
    {
        return familyName;
    }
    
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    
    public void setApproveTime(Date approveTime) 
    {
        this.approveTime = approveTime;
    }

    public Date getApproveTime() 
    {
        return approveTime;
    }
    
    public void setApproveBy(String approveBy) 
    {
        this.approveBy = approveBy;
    }

    public String getApproveBy() 
    {
        return approveBy;
    }

    @Override
    public String toString() {
        return "FamilyRegister{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", kinship='" + kinship + '\'' +
                ", occupation='" + occupation + '\'' +
                ", userType='" + userType + '\'' +
                ", familyName='" + familyName + '\'' +
                ", status='" + status + '\'' +
                ", approveTime=" + approveTime +
                ", approveBy='" + approveBy + '\'' +
                '}';
    }
}
