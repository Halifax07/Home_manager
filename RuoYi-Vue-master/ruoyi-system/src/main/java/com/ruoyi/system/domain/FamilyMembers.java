package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 家庭成员对象 family_members
 * 
 * @author ruoyi
 * @date 2025-09-07
 */
public class FamilyMembers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 家庭成员ID */
    private Integer id;

    /** 家庭ID */
    @Excel(name = "家庭ID")
    private Integer familyId;

    /** 成员姓名 */
    @Excel(name = "成员姓名")
    private String name;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 家庭身份（如：父亲/母亲/子女等） */
    @Excel(name = "家庭身份")
    private String kinship;

    /** 职业 */
    @Excel(name = "职业")
    private String occupation;

    /** 登录账号（唯一；可为空） */
    private String account;

    /** 密码哈希（建议用BCrypt/Argon2） */
    private String passwordHash;

    /** 系统角色 */
    @Excel(name = "系统角色")
    private String role;

    // 关联查询字段
    private String familyName;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }
    
    public void setFamilyId(Integer familyId) 
    {
        this.familyId = familyId;
    }

    public Integer getFamilyId() 
    {
        return familyId;
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
    
    public void setRole(String role) 
    {
        this.role = role;
    }

    public String getRole() 
    {
        return role;
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
        return "FamilyMembers{" +
                "id=" + id +
                ", familyId=" + familyId +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", kinship='" + kinship + '\'' +
                ", occupation='" + occupation + '\'' +
                ", account='" + account + '\'' +
                ", role='" + role + '\'' +
                ", familyName='" + familyName + '\'' +
                '}';
    }
}