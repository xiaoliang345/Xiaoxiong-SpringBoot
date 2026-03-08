/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50744
 Source Host           : localhost:3306
 Source Schema         : xiaoxiong

 Target Server Type    : MySQL
 Target Server Version : 50744
 File Encoding         : 65001

 Date: 08/03/2026 18:00:01
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID（同时作为权限ID）',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(4) NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` tinyint(4) NOT NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` tinyint(4) NOT NULL DEFAULT 0 COMMENT '菜单状态（0显示 1隐藏）',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识（权限码），如：system:user:add、*',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '备注',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE INDEX `uk_perms`(`perms`) USING BTREE COMMENT '权限标识唯一索引（可为空）',
  INDEX `idx_parent_id`(`parent_id`) USING BTREE COMMENT '父菜单索引',
  INDEX `idx_menu_type`(`menu_type`) USING BTREE COMMENT '类型索引',
  INDEX `idx_status`(`status`, `is_deleted`) USING BTREE COMMENT '状态索引'
) ENGINE = InnoDB AUTO_INCREMENT = 10000 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, '系统管理', 0, 1, 'system', NULL, '', '', 1, 0, 'M', 0, 1, NULL, 'system', 'admin', '2026-03-08 16:13:26', 'admin', '2026-03-08 16:13:26', '系统管理目录', 0);
INSERT INTO `sys_permission` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 0, 'C', 0, 1, 'system:user:list', 'user', 'admin', '2026-03-08 16:13:26', 'admin', '2026-03-08 16:13:26', '用户管理菜单', 0);
INSERT INTO `sys_permission` VALUES (1000, '用户查询', 100, 1, '#', '', '', '', 1, 0, 'F', 0, 1, 'system:user:query', '#', 'admin', '2026-03-08 16:13:26', 'admin', '2026-03-08 16:13:26', '', 0);
INSERT INTO `sys_permission` VALUES (1001, '用户新增', 100, 2, '#', '', '', '', 1, 0, 'F', 0, 1, 'system:user:add', '#', 'admin', '2026-03-08 16:13:26', 'admin', '2026-03-08 16:13:26', '', 0);
INSERT INTO `sys_permission` VALUES (1002, '用户修改', 100, 3, '#', '', '', '', 1, 0, 'F', 0, 1, 'system:user:edit', '#', 'admin', '2026-03-08 16:13:26', 'admin', '2026-03-08 16:13:26', '', 0);
INSERT INTO `sys_permission` VALUES (1003, '用户删除', 100, 4, '#', '', '', '', 1, 0, 'F', 0, 1, 'system:user:remove', '#', 'admin', '2026-03-08 16:13:26', 'admin', '2026-03-08 16:13:26', '', 0);
INSERT INTO `sys_permission` VALUES (9999, '全局权限', 0, 999, '#', '', '', '', 1, 0, 'F', 1, 1, '*', '#', 'admin', '2026-03-08 16:13:26', 'admin', '2026-03-08 16:13:26', '超级管理员全局权限', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色标识（Sa-Token使用），如：user, admin, super-admin',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_code` tinyint(4) NOT NULL COMMENT '角色代码（兼容旧系统），0-普通用户 1-管理员 2-超级管理员',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父角色ID（用于角色继承），NULL表示顶级角色',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `sort_order` int(11) NOT NULL DEFAULT 0 COMMENT '排序号（数字越小越靠前）',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_key`(`role_key`) USING BTREE COMMENT '角色标识唯一索引',
  UNIQUE INDEX `uk_role_code`(`role_code`) USING BTREE COMMENT '角色代码唯一索引',
  INDEX `idx_parent_id`(`parent_id`) USING BTREE COMMENT '父角色索引',
  INDEX `idx_status`(`status`, `is_deleted`) USING BTREE COMMENT '状态索引'
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'user', '普通用户', 0, NULL, '系统基础角色，所有用户默认拥有', 1, 1, 0, '2026-03-08 16:13:26', '2026-03-08 16:13:26');
INSERT INTO `sys_role` VALUES (2, 'admin', '管理员', 1, 1, '系统管理员，继承普通用户的所有权限', 2, 1, 0, '2026-03-08 16:13:26', '2026-03-08 16:13:26');
INSERT INTO `sys_role` VALUES (3, 'super-admin', '超级管理员', 2, 2, '超级管理员，继承管理员的所有权限', 3, 1, 0, '2026-03-08 16:13:26', '2026-03-08 16:13:26');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_permission`(`role_id`, `permission_id`) USING BTREE COMMENT '角色权限唯一索引',
  INDEX `idx_role_id`(`role_id`) USING BTREE COMMENT '角色ID索引',
  INDEX `idx_permission_id`(`permission_id`) USING BTREE COMMENT '权限ID索引'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 1, 1000, '2026-03-08 16:13:26');
INSERT INTO `sys_role_permission` VALUES (2, 2, 1001, '2026-03-08 16:13:26');
INSERT INTO `sys_role_permission` VALUES (3, 2, 1002, '2026-03-08 16:13:26');
INSERT INTO `sys_role_permission` VALUES (4, 2, 1003, '2026-03-08 16:13:26');
INSERT INTO `sys_role_permission` VALUES (5, 3, 9999, '2026-03-08 16:13:26');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码（加密）',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `role` tinyint(4) NOT NULL DEFAULT 0 COMMENT '0-普通用户 1-管理员 2-超级管理员',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '0-禁用 1-正常',
  `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE,
  UNIQUE INDEX `uk_phone`(`phone`) USING BTREE,
  UNIQUE INDEX `uk_email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'user01', '123456', '普通用户一', 'user01@test.com', '13800138001', '/avatar/default.png', 0, 1, 0, '2026-03-06 20:12:45', '2026-03-06 20:13:07');
INSERT INTO `sys_user` VALUES (2, 'user02', '123456', '普通用户二', 'user02@test.com', '13800138002', '/avatar/default.png', 0, 1, 0, '2026-03-06 20:12:45', '2026-03-06 20:13:11');
INSERT INTO `sys_user` VALUES (3, 'admin01', '123456', '系统管理员', 'admin01@test.com', '13800138003', '/avatar/admin.png', 1, 1, 0, '2026-03-06 20:12:45', '2026-03-06 20:13:13');
INSERT INTO `sys_user` VALUES (4, 'super01', '123456', '超级管理员', 'super01@test.com', '13800138004', '/avatar/super.png', 2, 1, 0, '2026-03-06 20:12:45', '2026-03-06 20:13:15');
INSERT INTO `sys_user` VALUES (5, 'deleted01', '123456', '已删除用户', 'deleted01@test.com', '13800138005', '/avatar/default.png', 0, 0, 1, '2026-03-06 20:12:45', '2026-03-06 20:13:16');

-- ----------------------------
-- Table structure for sys_user_resource_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_resource_role`;
CREATE TABLE `sys_user_resource_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `resource_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源类型，如：space, project, team等',
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `role_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资源角色标识，如：admin, member, viewer等',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_resource_role`(`user_id`, `resource_type`, `resource_id`) USING BTREE COMMENT '用户资源角色唯一索引（一个用户在同一个资源中只能有一个角色）',
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_resource`(`resource_type`, `resource_id`) USING BTREE COMMENT '资源索引',
  INDEX `idx_role_key`(`role_key`) USING BTREE COMMENT '角色标识索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户资源角色表（资源级权限）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_resource_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_role`(`user_id`, `role_id`) USING BTREE COMMENT '用户角色唯一索引',
  INDEX `idx_user_id`(`user_id`) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_role_id`(`role_id`) USING BTREE COMMENT '角色ID索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
