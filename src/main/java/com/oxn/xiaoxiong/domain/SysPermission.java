package com.oxn.xiaoxiong.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 系统权限表
 * 对应数据表：sys_permission
 */
@TableName(value = "sys_permission")
@Data
public class SysPermission {

    /**
     * 主键 ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 权限标识，如：user.add、user.get、art.*
     */
    private String permissionKey;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限说明
     */
    private String description;

    /**
     * 父级 ID，用于构建树形结构
     */
    private Long parentId;

    /**
     * 排序号，用于菜单排序
     */
    private Integer orderNum;

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
