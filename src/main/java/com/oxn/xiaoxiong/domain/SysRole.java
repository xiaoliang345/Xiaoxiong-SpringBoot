package com.oxn.xiaoxiong.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 系统角色
 * @TableName sys_role
 */
@TableName(value = "sys_role")
@Data
public class SysRole {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色标识（Sa-Token 使用），如：user, admin, super-admin
     */
    private String roleKey;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色代码（兼容旧系统），0-普通用户 1-管理员 2-超级管理员
     */
    private Integer roleCode;

    /**
     * 父角色ID（用于角色继承）
     */
    private Long parentId;

    private String description;

    private Integer sortOrder;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;

    private Date createTime;

    private Date updateTime;
}

