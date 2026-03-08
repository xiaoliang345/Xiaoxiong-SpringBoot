package com.oxn.xiaoxiong.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 角色-权限 关联表
 * 对应数据表：sys_role_permission
 */
@TableName(value = "sys_role_permission")
@Data
public class SysRolePermission {

    /**
     * 主键 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色 ID
     */
    private Long roleId;

    /**
     * 权限 ID
     */
    private Long permissionId;

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
