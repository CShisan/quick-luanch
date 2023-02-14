/*
 Navicat Premium Data Transfer

 Source Server         : quick-launch-demo
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : cshisanrds.mysql.rds.aliyuncs.com:3306
 Source Schema         : quick_launch_demo

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 14/02/2023 21:54:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for perm_resources
-- ----------------------------
DROP TABLE IF EXISTS `perm_resources`;
CREATE TABLE `perm_resources`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识，0未删除，1已删除',
  `editor` bigint(20) UNSIGNED NOT NULL COMMENT '编辑者ID',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `permission_id` bigint(20) UNSIGNED NOT NULL COMMENT '权限ID',
  `resource_id` bigint(20) UNSIGNED NOT NULL COMMENT '资源ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of perm_resources
-- ----------------------------
INSERT INTO `perm_resources` VALUES (1, 0, 9527, '2022-12-06 21:32:36', '2022-12-06 21:32:36', 6069541231838729871, 6069541234099932731);
INSERT INTO `perm_resources` VALUES (2, 0, 9527, '2022-12-06 21:32:37', '2022-12-06 21:32:37', 6069541231838729872, 6069541234099932732);
INSERT INTO `perm_resources` VALUES (3, 0, 9527, '2022-12-06 21:32:38', '2022-12-06 21:32:38', 6069541231838729873, 6069541234099932733);
INSERT INTO `perm_resources` VALUES (4, 0, 9527, '2022-12-06 21:32:40', '2022-12-06 21:32:40', 6069541231838729874, 6069541234099932734);
INSERT INTO `perm_resources` VALUES (5, 0, 9527, '2022-12-06 21:32:41', '2022-12-06 21:32:41', 6069541231838729875, 6069541234099932735);
INSERT INTO `perm_resources` VALUES (6, 0, 9527, '2022-12-06 21:32:42', '2022-12-06 21:32:42', 6069541231838729876, 6069541234099932736);
INSERT INTO `perm_resources` VALUES (7, 0, 9527, '2022-12-06 21:32:43', '2022-12-06 21:32:43', 6069541231838729877, 6069541234099932737);
INSERT INTO `perm_resources` VALUES (8, 0, 9527, '2022-12-06 21:32:45', '2022-12-06 21:32:45', 6069541231838729878, 6069541234099932738);
INSERT INTO `perm_resources` VALUES (9, 0, 9527, '2022-12-06 21:32:47', '2022-12-06 21:32:47', 6069541231838729896, 6069541234099932739);

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识，0未删除，1已删除',
  `editor` bigint(20) UNSIGNED NOT NULL COMMENT '编辑者ID',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `permission_id` bigint(20) UNSIGNED NOT NULL COMMENT '权限ID',
  `permission_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限名称',
  `permission_key` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限KEY',
  `pid` bigint(20) UNSIGNED NOT NULL COMMENT '父ID',
  `leaf_flag` tinyint(1) NOT NULL DEFAULT 1 COMMENT '叶子结点标记',
  `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '启用标识，0禁用，1启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES (1, 0, 9527, '2022-12-11 01:13:38', '2022-12-11 01:13:38', 6069541231838729871, '后台权限', 'backstage:home', 0, 0, 1);
INSERT INTO `permission` VALUES (2, 0, 9527, '2022-12-06 21:29:42', '2022-12-06 21:29:42', 6069541231838729872, '系统管理权限', 'bs:system', 6069541231838729871, 0, 1);
INSERT INTO `permission` VALUES (3, 0, 9527, '2022-12-06 21:29:45', '2022-12-06 21:29:45', 6069541231838729873, '日志管理权限', 'bs:log', 6069541231838729871, 0, 1);
INSERT INTO `permission` VALUES (4, 0, 9527, '2022-12-06 21:29:47', '2022-12-06 21:29:47', 6069541231838729874, '用户管理权限', 'bs:system:user', 6069541231838729872, 0, 1);
INSERT INTO `permission` VALUES (5, 0, 9527, '2022-12-06 21:29:47', '2022-12-06 21:29:47', 6069541231838729875, '角色管理权限', 'bs:system:role', 6069541231838729872, 0, 1);
INSERT INTO `permission` VALUES (6, 0, 9527, '2022-12-06 21:29:48', '2022-12-06 21:29:48', 6069541231838729876, '权限管理权限', 'bs:system:permission', 6069541231838729872, 0, 1);
INSERT INTO `permission` VALUES (7, 0, 9527, '2022-12-06 21:29:49', '2022-12-06 21:29:49', 6069541231838729877, '资源管理权限', 'bs:system:resources', 6069541231838729872, 0, 1);
INSERT INTO `permission` VALUES (8, 0, 9527, '2022-12-06 21:30:04', '2022-12-06 21:30:04', 6069541231838729878, '日志管理权限', 'bs:log:info', 6069541231838729873, 1, 1);
INSERT INTO `permission` VALUES (9, 0, 9527, '2022-12-06 21:30:19', '2022-12-06 21:30:19', 6069541231838729879, '用户信息插入权限', 'bs:system:user:insert', 6069541231838729874, 1, 1);
INSERT INTO `permission` VALUES (10, 0, 9527, '2022-12-06 21:30:21', '2022-12-06 21:30:21', 6069541231838729880, '用户信息删除权限', 'bs:system:user:delete', 6069541231838729874, 1, 1);
INSERT INTO `permission` VALUES (11, 0, 9527, '2022-12-06 21:30:22', '2022-12-06 21:30:22', 6069541231838729881, '用户信息更新权限', 'bs:system:user:update', 6069541231838729874, 1, 1);
INSERT INTO `permission` VALUES (12, 0, 9527, '2022-12-06 21:30:24', '2022-12-06 21:30:24', 6069541231838729882, '用户信息查询权限', 'bs:system:user:select', 6069541231838729874, 1, 1);
INSERT INTO `permission` VALUES (13, 0, 9527, '2022-12-06 21:30:25', '2022-12-06 21:30:25', 6069541231838729883, '角色信息插入权限', 'bs:system:role:insert', 6069541231838729875, 1, 1);
INSERT INTO `permission` VALUES (14, 0, 9527, '2022-12-06 21:30:27', '2022-12-06 21:30:27', 6069541231838729884, '角色信息删除权限', 'bs:system:role:delete', 6069541231838729875, 1, 1);
INSERT INTO `permission` VALUES (15, 0, 9527, '2022-12-06 21:30:28', '2022-12-06 21:30:28', 6069541231838729885, '角色信息更新权限', 'bs:system:role:update', 6069541231838729875, 1, 1);
INSERT INTO `permission` VALUES (16, 0, 9527, '2022-12-06 21:30:30', '2022-12-06 21:30:30', 6069541231838729886, '角色信息查询权限', 'bs:system:role:select', 6069541231838729875, 1, 1);
INSERT INTO `permission` VALUES (17, 0, 9527, '2022-12-06 21:30:31', '2022-12-06 21:30:31', 6069541231838729887, '权限信息插入权限', 'bs:system:permission:insert', 6069541231838729876, 1, 1);
INSERT INTO `permission` VALUES (18, 0, 9527, '2022-12-06 21:30:33', '2022-12-06 21:30:33', 6069541231838729888, '权限信息删除权限', 'bs:system:permission:delete', 6069541231838729876, 1, 1);
INSERT INTO `permission` VALUES (19, 0, 9527, '2022-12-06 21:30:34', '2022-12-06 21:30:34', 6069541231838729889, '权限信息更新权限', 'bs:system:permission:update', 6069541231838729876, 1, 1);
INSERT INTO `permission` VALUES (20, 0, 9527, '2022-12-06 21:30:36', '2022-12-06 21:30:36', 6069541231838729890, '权限信息查询权限', 'bs:system:permission:select', 6069541231838729876, 1, 1);
INSERT INTO `permission` VALUES (21, 0, 9527, '2022-12-06 21:30:37', '2022-12-06 21:30:37', 6069541231838729891, '资源信息插入权限', 'bs:system:resources:insert', 6069541231838729877, 1, 1);
INSERT INTO `permission` VALUES (22, 0, 9527, '2022-12-06 21:30:40', '2022-12-06 21:30:40', 6069541231838729892, '资源信息删除权限', 'bs:system:resources:delete', 6069541231838729877, 1, 1);
INSERT INTO `permission` VALUES (23, 0, 9527, '2022-12-06 21:30:41', '2022-12-06 21:30:41', 6069541231838729893, '资源信息更新权限', 'bs:system:resources:update', 6069541231838729877, 1, 1);
INSERT INTO `permission` VALUES (24, 0, 9527, '2022-12-06 21:30:43', '2022-12-06 21:30:43', 6069541231838729894, '资源信息查询权限', 'bs:system:resources:select', 6069541231838729877, 1, 1);
INSERT INTO `permission` VALUES (25, 0, 9527, '2022-12-11 01:13:41', '2022-12-11 01:13:41', 6069541231838729895, '后端权限', 'backend:all', 0, 0, 1);
INSERT INTO `permission` VALUES (26, 0, 9527, '2022-12-06 21:29:38', '2022-12-06 21:29:38', 6069541231838729896, '后端测试权限', 'be:test', 6069541231838729895, 1, 1);

-- ----------------------------
-- Table structure for resources
-- ----------------------------
DROP TABLE IF EXISTS `resources`;
CREATE TABLE `resources`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识，0未删除，1已删除',
  `editor` bigint(20) UNSIGNED NOT NULL COMMENT '编辑者ID',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `resource_id` bigint(20) UNSIGNED NOT NULL COMMENT '资源ID',
  `type` tinyint(2) NOT NULL COMMENT '资源类型，1前端，2后端',
  `path` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源路径',
  `description` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源描述',
  `pid` bigint(20) UNSIGNED NOT NULL COMMENT '父id',
  `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '启用标识，0禁用，1启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资源表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of resources
-- ----------------------------
INSERT INTO `resources` VALUES (1, 0, 9527, '2022-12-11 01:13:53', '2022-12-11 01:13:53', 6069541234099932731, 1, '/qiuck-launch', '后台主页', 0, 1);
INSERT INTO `resources` VALUES (2, 0, 9527, '2022-12-06 21:32:00', '2022-12-06 21:32:00', 6069541234099932732, 1, '/system', '系统管理页面', 6069541234099932731, 1);
INSERT INTO `resources` VALUES (3, 0, 9527, '2022-12-06 21:32:01', '2022-12-06 21:32:01', 6069541234099932733, 1, '/log', '日志管理页面', 6069541234099932731, 1);
INSERT INTO `resources` VALUES (4, 0, 9527, '2022-12-06 21:32:02', '2022-12-06 21:32:02', 6069541234099932734, 1, '/system/user', '用户信息管理页面', 6069541234099932732, 1);
INSERT INTO `resources` VALUES (5, 0, 9527, '2022-12-06 21:32:03', '2022-12-06 21:32:03', 6069541234099932735, 1, '/system/role', '角色信息管理页面', 6069541234099932732, 1);
INSERT INTO `resources` VALUES (6, 0, 9527, '2022-12-06 21:32:05', '2022-12-06 21:32:05', 6069541234099932736, 1, '/system/permission', '权限信息管理页面', 6069541234099932732, 1);
INSERT INTO `resources` VALUES (7, 0, 9527, '2022-12-06 21:32:07', '2022-12-06 21:32:07', 6069541234099932737, 1, '/system/resources', '资源信息管理页面', 6069541234099932732, 1);
INSERT INTO `resources` VALUES (8, 0, 9527, '2022-12-06 21:32:13', '2022-12-06 21:32:13', 6069541234099932738, 1, '/log/info', '日志信息管理页面', 6069541234099932733, 1);
INSERT INTO `resources` VALUES (9, 0, 9527, '2022-12-11 01:13:55', '2022-12-11 01:13:55', 6069541234099932739, 2, '/test/*', '后端-测试路径', 0, 1);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识，0未删除，1已删除',
  `editor` bigint(20) UNSIGNED NOT NULL COMMENT '编辑者ID',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色ID',
  `role_key` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色KEY',
  `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '启用标识，0禁用，1启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 0, 9527, '2022-12-06 21:27:01', '2022-12-06 21:27:01', 6069541231347711151, 'admin', '超级管理员', 1);
INSERT INTO `role` VALUES (2, 0, 9527, '2022-12-06 21:27:03', '2022-12-06 21:27:03', 6069541231347711152, 'manager', '管理员', 1);
INSERT INTO `role` VALUES (3, 0, 9527, '2022-12-06 21:27:06', '2022-12-06 21:27:06', 6069541231347711153, 'maintainer', '系统维护员', 1);
INSERT INTO `role` VALUES (4, 0, 9527, '2022-12-06 21:27:27', '2022-12-06 21:27:27', 6069541231347711154, 'normal', '普通用户', 1);

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识，0未删除，1已删除',
  `editor` bigint(20) UNSIGNED NOT NULL COMMENT '编辑者ID',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色权限关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES (1, 0, 9527, '2022-12-06 21:35:36', '2022-12-06 21:35:36', 6069541231347711151, 6069541231838729871);
INSERT INTO `role_permission` VALUES (2, 0, 9527, '2022-12-12 17:27:22', '2022-12-12 17:27:22', 6069541231347711151, 6069541231838729895);
INSERT INTO `role_permission` VALUES (31, 1, 9527, '2022-12-13 15:00:08', '2022-12-13 15:00:08', 6069541231347711152, 6069541231838729872);
INSERT INTO `role_permission` VALUES (32, 1, 9527, '2022-12-13 15:11:29', '2022-12-13 15:11:29', 6069541231347711152, 6069541231838729871);
INSERT INTO `role_permission` VALUES (33, 1, 9527, '2022-12-13 15:11:42', '2022-12-13 15:11:42', 6069541231347711152, 6069541231838729872);
INSERT INTO `role_permission` VALUES (34, 0, 9527, '2022-12-13 15:11:42', '2022-12-13 15:11:42', 6069541231347711152, 6069541231838729871);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识，0未删除，1已删除',
  `editor` bigint(20) UNSIGNED NOT NULL COMMENT '编辑者ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `uid` bigint(20) UNSIGNED NOT NULL COMMENT 'UID',
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '手机号码',
  `email` varchar(52) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '头像URL',
  `open_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '微信openid',
  `enable` tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '启用标识，0禁用，1启用',
  `locked` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '锁定标识，0未锁定，1锁定',
  `login_last_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `login_last_ip` varchar(39) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最后登录IP',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 0, 9627, '2022-06-12 17:57:24', '2023-02-10 21:29:05', 9527, 'xxx', '$2a$10$2o2/J0izV4dC5lPMmZ9ZDeMoOpjwgF9H9nPBIo/QsJXGdodg8Jbom', '13800000000', '123@qq.com', '', '', 1, 0, '2023-02-10 21:29:05', '0:0:0:0:0:0:0:1');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标识，0未删除，1已删除',
  `editor` bigint(20) UNSIGNED NOT NULL COMMENT '编辑者ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `uid` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 0, 9527, '2022-12-18 01:09:19', '2022-12-18 01:09:22', 9527, 6069541231347711151);

SET FOREIGN_KEY_CHECKS = 1;
