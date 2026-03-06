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

 Date: 06/03/2026 23:33:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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

SET FOREIGN_KEY_CHECKS = 1;
