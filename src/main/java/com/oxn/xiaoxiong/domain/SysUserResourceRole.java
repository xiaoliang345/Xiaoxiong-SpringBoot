package com.oxn.xiaoxiong.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户-资源-角色 关联表
 * 可用于对某些资源（如专栏、项目等）授予特定角色权限
 * 对应数据表：sys_user_resource_role
 */
@TableName(value = "sys_user_resource_role")
@Data
public class SysUserResourceRole {

    /**
     * 主键 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 资源 ID（如专栏、项目等）
     */
    private Long resourceId;

    /**
     * 资源类型（如：project、department 等）
     */
    private String resourceType;

    /**
     * 角色 ID
     */
    private Long roleId;

    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;

    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
