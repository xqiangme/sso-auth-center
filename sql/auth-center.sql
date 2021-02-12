/*
 程序员小强统一认证中心SQL脚本
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sso_dept
-- ----------------------------
DROP TABLE IF EXISTS `sso_dept`;
CREATE TABLE `sso_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `sys_code` varchar(36) NOT NULL DEFAULT '' COMMENT '系统编码',
  `dept_name` varchar(255) NOT NULL DEFAULT '' COMMENT '部门名称',
  `dept_parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父部门ID 0-根部门',
  `sort_num` int(10) NOT NULL DEFAULT '1' COMMENT '顺序',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0-启用;1-停用',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志 0-正常;1-删除',
  `remarks` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) NOT NULL DEFAULT '' COMMENT '最后修改者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dept_id`),
  KEY `idx_sys_code` (`sys_code`) USING BTREE,
  KEY `idx_parent_id` (`dept_parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2000 DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- ----------------------------
-- Records of sso_dept
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sso_dept_tree_path
-- ----------------------------
DROP TABLE IF EXISTS `sso_dept_tree_path`;
CREATE TABLE `sso_dept_tree_path` (
  `ancestor` bigint(20) NOT NULL COMMENT '祖先',
  `descendant` bigint(20) NOT NULL COMMENT '后代',
  PRIMARY KEY (`ancestor`,`descendant`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='部门祖先/后代关系表';

-- ----------------------------
-- Records of sso_dept_tree_path
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sso_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sso_login_log`;
CREATE TABLE `sso_login_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `request_id` varchar(36) NOT NULL COMMENT '请求标识',
  `username` varchar(32) NOT NULL DEFAULT '' COMMENT '用户登录名',
  `source_ip` varchar(36) NOT NULL DEFAULT '' COMMENT '来源IP',
  `source_address` varchar(128) NOT NULL DEFAULT '' COMMENT '来源地址',
  `browser_name` varchar(50) NOT NULL DEFAULT '' COMMENT '浏览器',
  `operate_system` varchar(50) NOT NULL DEFAULT '' COMMENT '操作系统',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '登录状态 0-成功;1-失败',
  `login_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '登录时间戳',
  `login_out_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '主动退出时间戳',
  `expire_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '失效时间戳',
  `operate_msg` varchar(255) NOT NULL DEFAULT '' COMMENT '返回结果',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_request_id` (`request_id`) USING BTREE,
  KEY `idx_username` (`username`) USING BTREE,
  KEY `idx_login_time` (`login_time`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';



-- ----------------------------
-- Table structure for sso_menu
-- ----------------------------
DROP TABLE IF EXISTS `sso_menu`;
CREATE TABLE `sso_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(64) NOT NULL COMMENT '菜单名称',
  `sys_code` varchar(36) NOT NULL DEFAULT '' COMMENT '系统编码',
  `menu_type` varchar(10) NOT NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `menu_parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父菜单ID 0-顶级',
  `sort_num` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) NOT NULL DEFAULT '' COMMENT '菜单路由地址',
  `icon` varchar(128) NOT NULL DEFAULT '' COMMENT '菜单图标',
  `component` varchar(512) NOT NULL DEFAULT '' COMMENT '页面组件路径,>分隔可跨级多个',
  `permissions` varchar(512) NOT NULL DEFAULT '' COMMENT '权限标识,多个|分隔',
  `visible` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否显示 0-显示;1-隐藏',
  `use_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '使用类型 0-授权访问;1-开放访问;',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '菜单状态 0-正常;1停用',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志 0-正常;1-删除',
  `remarks` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) NOT NULL DEFAULT '' COMMENT '最后修改者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`menu_id`) USING BTREE,
  KEY `idx_sys_code_del_flag` (`sys_code`,`del_flag`) USING BTREE,
  KEY `idx_menu_parent_id` (`menu_parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3000 DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- ----------------------------
-- Records of sso_menu
-- ----------------------------
BEGIN;
INSERT INTO `sso_menu` VALUES (1, '我的平台', 'auth-center', 'C', 0, 1, '/myPlatform/index', '', '/views/sso/platform/platformMy', '', 0, 1, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 22:56:29');
INSERT INTO `sso_menu` VALUES (2, '平台管理', 'auth-center', 'C', 0, 2, '/platformMgmt/index', '', '/views/sso/platform/platformMgmt', 'system:mgmtList', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 22:56:29');
INSERT INTO `sso_menu` VALUES (3, '系统监控', 'auth-center', 'C', 0, 3, '/userOnline/index', '', '', '', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 22:56:29');
INSERT INTO `sso_menu` VALUES (4, '博客推荐', 'auth-center', 'M', 0, 4, 'https://blog.csdn.net/qq_38011415', '', '', '', 0, 0, 0, 0, '', 'system', 'admin', '2021-02-09 22:56:29', '2021-02-12 17:23:42');
INSERT INTO `sso_menu` VALUES (5, '平台详情', 'auth-center', 'C', 2, 1, '/platformMgmt/platformDetail', 'guide', '/views/sso/platform/platformDetail', 'system:getDetailBySysCode', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:23');
INSERT INTO `sso_menu` VALUES (6, '用户管理', 'auth-center', 'C', 2, 2, '/user/index', 'userMgmt', '/views/sso/user/index', 'user:listPage', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:23');
INSERT INTO `sso_menu` VALUES (7, '部门管理', 'auth-center', 'C', 2, 3, '/dept/index', 'tree', '/views/sso/dept/index', 'dept:listDeptMgmtTree', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:23');
INSERT INTO `sso_menu` VALUES (8, '菜单管理', 'auth-center', 'C', 2, 4, '/menu/index', 'tree-table', '/views/sso/menu/index', 'menu:listMenuTree', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:23');
INSERT INTO `sso_menu` VALUES (9, '角色管理', 'auth-center', 'C', 2, 5, '/role/index', 'role', '/views/sso/role/index', 'role:listPage', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:23');
INSERT INTO `sso_menu` VALUES (10, '新增平台', 'auth-center', 'F', 2, 0, '', '', '', 'system:add', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:23');
INSERT INTO `sso_menu` VALUES (11, '修改系统', 'auth-center', 'F', 5, 1, '', '', '', 'system:update', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:40');
INSERT INTO `sso_menu` VALUES (12, '修改秘钥', 'auth-center', 'F', 5, 1, '', '', '', 'system:updateSecret', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:40');
INSERT INTO `sso_menu` VALUES (13, '系统管理权设置', 'auth-center', 'F', 5, 3, '', '', '', 'systemMgmt:updateSystemMgmt', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:40');
INSERT INTO `sso_menu` VALUES (14, '新增用户', 'auth-center', 'F', 6, 1, '', '', '', 'user:add', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:53');
INSERT INTO `sso_menu` VALUES (15, '修改用户', 'auth-center', 'F', 6, 2, '', '', '', 'user:update', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:53');
INSERT INTO `sso_menu` VALUES (16, '删除用户', 'auth-center', 'F', 6, 3, '', '', '', 'user:delete', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:53');
INSERT INTO `sso_menu` VALUES (17, '重置密码', 'auth-center', 'F', 6, 4, '', '', '', 'user:resetPwd', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:09:53');
INSERT INTO `sso_menu` VALUES (18, '新增部门', 'auth-center', 'F', 7, 1, '', '', '', 'dept:add', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:10:00');
INSERT INTO `sso_menu` VALUES (19, '修改部门', 'auth-center', 'F', 7, 2, '', '', '', 'dept:update', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:10:00');
INSERT INTO `sso_menu` VALUES (20, '删除部门', 'auth-center', 'F', 7, 3, '', '', '', 'dept:delete', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:10:00');
INSERT INTO `sso_menu` VALUES (21, '新增菜单', 'auth-center', 'F', 8, 1, '', '', '', 'menu:add', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:10:18');
INSERT INTO `sso_menu` VALUES (22, '修改菜单', 'auth-center', 'F', 8, 2, '', '', '', 'menu:update', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:10:18');
INSERT INTO `sso_menu` VALUES (23, '删除菜单', 'auth-center', 'F', 8, 3, '', '', '', 'menu:delete', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:10:18');
INSERT INTO `sso_menu` VALUES (24, '新增角色', 'auth-center', 'F', 9, 1, '', '', '', 'role:add', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:10:24');
INSERT INTO `sso_menu` VALUES (25, '修改角色', 'auth-center', 'F', 9, 2, '', '', '', 'role:update', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:10:24');
INSERT INTO `sso_menu` VALUES (26, '删除角色', 'auth-center', 'F', 9, 3, '', '', '', 'role:delete', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:10:24');
INSERT INTO `sso_menu` VALUES (27, '查看已绑用户', 'auth-center', 'F', 9, 4, '', '', '', 'user:listPage', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:10:24');
INSERT INTO `sso_menu` VALUES (28, '移除角色用户关系', 'auth-center', 'F', 9, 0, '', '', '', 'role:removeUser', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:10:45');
INSERT INTO `sso_menu` VALUES (29, '登录日志', 'auth-center', 'C', 3, 2, '/log/index', 'log', '', 'login:log:listPage', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:11:07');
INSERT INTO `sso_menu` VALUES (30, '在线用户', 'auth-center', 'C', 3, 1, '/userOnline/index', 'onlineUser', '', 'user:online:listPage', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:11:09');
INSERT INTO `sso_menu` VALUES (31, '查看已登录系统', 'auth-center', 'F', 30, 1, '', '', '', 'user:online:listOnlineSys', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:11:37');
INSERT INTO `sso_menu` VALUES (32, '强退用户', 'auth-center', 'F', 30, 2, '', '', '', 'user:online:retreatUser', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:11:40');
INSERT INTO `sso_menu` VALUES (33, '订单管理', 'sys-client01', 'C', 0, 3, '/order/manger', 'dashboard', 'Layout>/view/order/index', 'order:list|order:test|my:test', 0, 1, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 22:56:29');
INSERT INTO `sso_menu` VALUES (34, '销售管理', 'sys-client01', 'C', 0, 6, '/sales/manger', 'list', '/view/sales/index', 'sales:manger', 0, 1, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 22:56:29');
INSERT INTO `sso_menu` VALUES (35, '财务管理', 'sys-client01', 'C', 0, 5, '/finance/manger', 'post', '/view/finance/index', 'finance:manger', 0, 1, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 22:56:29');
INSERT INTO `sso_menu` VALUES (36, '订单新增', 'sys-client01', 'F', 33, 0, '', '', '', 'ordertwo', 0, 0, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 23:11:46');
INSERT INTO `sso_menu` VALUES (37, '系统2测试菜单', 'sys-client02', 'C', 0, 0, '/sys02/test', 'dashboard', '/view/test', 'sys02:test', 0, 1, 0, 0, '', 'system', 'system', '2021-02-09 22:56:29', '2021-02-09 22:56:29');
COMMIT;

-- ----------------------------
-- Table structure for sso_online_user
-- ----------------------------
DROP TABLE IF EXISTS `sso_online_user`;
CREATE TABLE `sso_online_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `request_id` varchar(36) NOT NULL COMMENT '请求标识',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `source_ip` varchar(36) NOT NULL DEFAULT '' COMMENT '来源IP',
  `source_address` varchar(128) NOT NULL DEFAULT '' COMMENT '来源地址',
  `browser_name` varchar(50) NOT NULL DEFAULT '' COMMENT '浏览器',
  `operate_system` varchar(50) NOT NULL DEFAULT '' COMMENT '操作系统',
  `login_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '登录时间戳',
  `login_out_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '主动退出时间戳',
  `expire_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '失效时间戳',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_request_id` (`request_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`),
  KEY `idx_expire_time` (`expire_time`) USING BTREE,
  KEY `idx_login_time` (`login_time`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='在线用户';


-- ----------------------------
-- Table structure for sso_role
-- ----------------------------
DROP TABLE IF EXISTS `sso_role`;
CREATE TABLE `sso_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `sys_code` varchar(36) NOT NULL DEFAULT '' COMMENT '系统编码',
  `role_name` varchar(36) NOT NULL DEFAULT '' COMMENT '角色名称',
  `role_key` varchar(64) NOT NULL DEFAULT '' COMMENT '角色标识key',
  `status` tinyint(10) NOT NULL DEFAULT '0' COMMENT '状态 0-正常;1-停用',
  `sort_num` int(4) NOT NULL DEFAULT '0' COMMENT '显示顺序',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志 0-正常;1-删除',
  `remarks` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) NOT NULL DEFAULT '' COMMENT '最后修改者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  KEY `idx_role_name` (`role_name`) USING BTREE,
  KEY `idx_sys_code` (`sys_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4004 DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

-- ----------------------------
-- Records of sso_role
-- ----------------------------
BEGIN;
INSERT INTO `sso_role` VALUES (4000, 'auth-center', '认证中心管理员', 'admin', 0, 1, 0, '拥有认证中心所有权限+所有平台的管理权限', 'admin', 'admin', '2021-02-10 17:51:37', '2021-02-12 16:54:32');
INSERT INTO `sso_role` VALUES (4001, 'auth-center', '平台管理员', '', 0, 2, 0, '拥有平台管理的功能权限+允许管理哪些平台还需添加数据权限', 'admin', 'admin', '2021-02-10 17:52:00', '2021-02-12 16:54:31');
INSERT INTO `sso_role` VALUES (4002, 'auth-center', '监控管理员', '', 0, 3, 0, '可查看在线用户记录以及+剔除用户', 'admin', 'admin', '2021-02-10 17:55:39', '2021-02-12 16:54:29');
INSERT INTO `sso_role` VALUES (4003, 'auth-center', '用户管理员', '', 0, 4, 0, '仅有平台详情查看+用户管理权限', 'admin', 'admin', '2021-02-10 17:57:03', '2021-02-12 16:54:27');
COMMIT;

-- ----------------------------
-- Table structure for sso_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sso_role_menu`;
CREATE TABLE `sso_role_menu` (
  `sys_code` varchar(36) NOT NULL DEFAULT '' COMMENT '系统编码',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关系表';

-- ----------------------------
-- Records of sso_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 1, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 2, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 3, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 4, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 5, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 6, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 7, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 8, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 9, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 10, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 11, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 12, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 13, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 14, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 15, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 16, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 17, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 18, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 19, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 20, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 21, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 22, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 23, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 24, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 25, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 26, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 27, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 28, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 29, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 30, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 31, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4000, 32, '2021-02-10 17:53:58', '2021-02-10 17:53:58');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 2, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 5, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 6, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 7, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 8, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 9, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 10, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 11, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 12, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 13, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 14, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 15, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 16, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 17, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 18, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 19, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 20, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 21, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 22, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 23, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 24, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 25, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 26, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 27, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4001, 28, '2021-02-10 17:55:20', '2021-02-10 17:55:20');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4002, 3, '2021-02-10 19:03:43', '2021-02-10 19:03:43');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4002, 29, '2021-02-10 19:03:43', '2021-02-10 19:03:43');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4002, 30, '2021-02-10 19:03:43', '2021-02-10 19:03:43');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4002, 31, '2021-02-10 19:03:43', '2021-02-10 19:03:43');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4002, 32, '2021-02-10 19:03:43', '2021-02-10 19:03:43');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4003, 2, '2021-02-10 18:14:18', '2021-02-10 18:14:18');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4003, 5, '2021-02-10 18:14:18', '2021-02-10 18:14:18');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4003, 6, '2021-02-10 18:14:18', '2021-02-10 18:14:18');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4003, 14, '2021-02-10 18:14:18', '2021-02-10 18:14:18');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4003, 15, '2021-02-10 18:14:18', '2021-02-10 18:14:18');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4003, 16, '2021-02-10 18:14:18', '2021-02-10 18:14:18');
INSERT INTO `sso_role_menu` VALUES ('auth-center', 4003, 17, '2021-02-10 18:14:18', '2021-02-10 18:14:18');
COMMIT;

-- ----------------------------
-- Table structure for sso_system
-- ----------------------------
DROP TABLE IF EXISTS `sso_system`;
CREATE TABLE `sso_system` (
  `sys_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sys_code` varchar(36) NOT NULL DEFAULT '' COMMENT '系统编码',
  `sys_name` varchar(128) NOT NULL DEFAULT '' COMMENT '系统名称',
  `sys_url` varchar(500) NOT NULL DEFAULT '' COMMENT '系统链接',
  `sys_icon` varchar(256) NOT NULL DEFAULT '' COMMENT '系统图标',
  `sys_env` tinyint(4) NOT NULL DEFAULT '0' COMMENT '系统环境 0-测试 1-beta 2-生产',
  `sort_num` tinyint(4) NOT NULL DEFAULT '0' COMMENT '排序，数字越小排在越前面',
  `remarks` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `sign_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '签名类型  0-无;1-MD5;2-RSA;',
  `public_key` varchar(2080) NOT NULL DEFAULT '' COMMENT '网关验签公钥',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 0-正常;1-停用',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志 0-正常;1-删除',
  `create_by` varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) NOT NULL DEFAULT '' COMMENT '最后修改者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`sys_id`) USING BTREE,
  UNIQUE KEY `uniq_syscode` (`sys_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1007 DEFAULT CHARSET=utf8mb4 COMMENT='目标系统表';

-- ----------------------------
-- Records of sso_system
-- ----------------------------
BEGIN;
INSERT INTO `sso_system` VALUES (1000, 'auth-center', '认证中心', '', 'https://img-blog.csdnimg.cn/20210208164649533.jpg', 0, 3, '', 0, '', 0, 0, 'system', 'system', '2020-12-29 10:54:27', '2021-02-09 20:54:50');
INSERT INTO `sso_system` VALUES (1001, 'sys-client01', '测试子系统1', 'http://www.sysclient1.com:8801', 'https://img-blog.csdnimg.cn/20210208165059482.png', 0, 1, '', 1, '1234567890', 0, 0, 'system', 'admin', '2021-02-07 14:43:14', '2021-02-12 17:17:08');
INSERT INTO `sso_system` VALUES (1002, 'sys-client02', '测试子系统2', 'http://www.sysclient2.com:8802', 'https://img-blog.csdnimg.cn/20210208165118817.png', 0, 2, '', 2, 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcHtasBWFTsGsgCThmGTZ5qLHsRR29cV7kGodMmSHoCUqed9Z23oBOKqZw4J9bLHLTpEGtZ0aBjiLl0jQQzriWQt4MC5PI2BISqw7hTNx0c2tYJjx91/EKr9jhC4d9tMWct5NOMKBgiZGlVVeODQspZQUbF+cxg8GN7RqUsZtL4wIDAQAB', 0, 0, 'system', 'admin', '2021-02-07 14:43:44', '2021-02-12 17:19:23');
INSERT INTO `sso_system` VALUES (1003, 'my-csdn', '小强博客', 'https://blog.csdn.net/qq_38011415', 'https://img-blog.csdnimg.cn/20210208164723932.jpg', 0, 4, '', 0, '', 0, 0, 'system', 'system', '2021-01-28 10:47:07', '2021-02-09 20:53:08');
INSERT INTO `sso_system` VALUES (1004, 'monitor', '监控平台', 'https://blog.csdn.net/qq_38011415/article/details/113745126', 'https://img-blog.csdnimg.cn/20210208164723951.jpg', 0, 8, '', 0, '', 0, 0, 'system', 'system', '2021-02-06 15:33:37', '2021-02-09 20:55:00');
INSERT INTO `sso_system` VALUES (1005, 'release', '发布系统', 'https://blog.csdn.net/qq_38011415/article/details/113745126', 'https://img-blog.csdnimg.cn/20210208164723956.jpg', 0, 9, '', 0, '', 0, 0, 'system', 'system', '2021-02-06 15:34:22', '2021-02-09 20:55:00');
INSERT INTO `sso_system` VALUES (1006, 'xqiangme', '小强GitHub', 'https://github.com/xqiangme', 'https://img-blog.csdnimg.cn/2021020816472446.png', 0, 10, '', 0, '', 0, 0, 'system', 'system', '2021-02-06 15:41:49', '2021-02-09 20:53:20');
COMMIT;

-- ----------------------------
-- Table structure for sso_system_manager
-- ----------------------------
DROP TABLE IF EXISTS `sso_system_manager`;
CREATE TABLE `sso_system_manager` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `sys_code` varchar(36) NOT NULL DEFAULT '' COMMENT '系统编码',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 0-启用;1-停用',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志 0-正常;1-删除',
  `create_by` varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) NOT NULL DEFAULT '' COMMENT '最后修改者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_sys_code` (`sys_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='平台管理员关系表';

-- ----------------------------
-- Records of sso_system_manager
-- ----------------------------
BEGIN;
INSERT INTO `sso_system_manager` VALUES (1, 1003, 'sys-client01', 0, 0, 'admin', 'admin', '2021-02-10 18:09:26', '2021-02-10 18:09:26');
INSERT INTO `sso_system_manager` VALUES (2, 1002, 'sys-client01', 0, 0, 'admin', 'admin', '2021-02-10 18:09:50', '2021-02-10 18:09:50');
COMMIT;

-- ----------------------------
-- Table structure for sso_user
-- ----------------------------
DROP TABLE IF EXISTS `sso_user`;
CREATE TABLE `sso_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(32) NOT NULL DEFAULT '' COMMENT '用户登录名',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  `nick_name` varchar(32) NOT NULL DEFAULT '' COMMENT '用户昵称',
  `real_name` varchar(32) NOT NULL DEFAULT '' COMMENT '真实姓名',
  `sex` tinyint(1) NOT NULL DEFAULT '2' COMMENT '用户性别 0-未知;1-男;2-女',
  `phone` varchar(32) NOT NULL DEFAULT '' COMMENT '手机号码',
  `email` varchar(255) NOT NULL DEFAULT '' COMMENT '邮箱',
  `avatar` varchar(255) NOT NULL DEFAULT '' COMMENT '头像',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 0-正常;1-停用',
  `login_ip` varchar(50) NOT NULL DEFAULT '' COMMENT '最后登录IP',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志 0-正常;1-删除',
  `remarks` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_by` varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) NOT NULL DEFAULT '' COMMENT '最后修改者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`),
  KEY `idx_username` (`username`) USING BTREE,
  KEY `idx_phone` (`phone`) USING BTREE,
  KEY `idx_email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of sso_user
-- ----------------------------
BEGIN;
INSERT INTO `sso_user` VALUES (1000, 'admin', '$2a$10$SCmZhgUB5unJ480A42PWRe.JBfUEFiYxoECw7ys25QoGwRBuCa7ie', 'admin', 'admin', 1, '15xxxxxxxxx', 'admin@163.com', '', 0, '0:0:0:0:0:0:0:1', '2021-02-12 17:22:41', 0, '', 'system', 'system', '2021-02-09 22:56:38', '2021-02-12 17:22:41');
INSERT INTO `sso_user` VALUES (1001, 'ssoAdmin', '$2a$10$pGyRaAzJn2OuW9ePSuTZWeCdInCayweMHdmjSlpipOcorsotVMxne', '认证中心管理', '认证中心管理员', 1, '13237717262', '', '', 0, '0:0:0:0:0:0:0:1', '2021-02-10 17:59:43', 0, '认证中心管理员', 'admin', '认证中心管理员', '2021-02-10 17:58:11', '2021-02-10 18:04:01');
INSERT INTO `sso_user` VALUES (1002, 'userMgmt', '$2a$10$PltBdy7g/MIi.PyoOGpKMuUD0Tny/veX00XhVL6qVxJXA4mVgVbGa', '用户管理员', '', 0, '13535717263', '', '', 0, '0:0:0:0:0:0:0:1', '2021-02-10 18:14:37', 0, '', 'admin', 'admin', '2021-02-10 18:06:29', '2021-02-10 18:14:36');
INSERT INTO `sso_user` VALUES (1003, 'platformMgmt', '$2a$10$2P8CDGQpDoapyXvBlkF0i.YV7z4lPM3JzKwJn.TwTwK4GWmnE/m8m', '平台管理员', '', 0, '13325777265', '', '', 0, '', NULL, 0, '', 'admin', 'admin', '2021-02-10 18:07:21', '2021-02-10 18:07:21');
COMMIT;

-- ----------------------------
-- Table structure for sso_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sso_user_dept`;
CREATE TABLE `sso_user_dept` (
  `sys_code` varchar(36) NOT NULL COMMENT '系统编码',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户部门关系表';

-- ----------------------------
-- Records of sso_user_dept
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sso_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sso_user_role`;
CREATE TABLE `sso_user_role` (
  `sys_code` varchar(36) NOT NULL DEFAULT '' COMMENT '系统编码',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

-- ----------------------------
-- Records of sso_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sso_user_role` VALUES ('auth-center', 1001, 4000, '2021-02-10 18:04:01', '2021-02-10 18:04:01');
INSERT INTO `sso_user_role` VALUES ('auth-center', 1002, 4003, '2021-02-10 18:06:29', '2021-02-10 18:06:29');
INSERT INTO `sso_user_role` VALUES ('auth-center', 1003, 4001, '2021-02-10 18:07:21', '2021-02-10 18:07:21');
COMMIT;

-- ----------------------------
-- Table structure for sso_user_system
-- ----------------------------
DROP TABLE IF EXISTS `sso_user_system`;
CREATE TABLE `sso_user_system` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `sys_code` varchar(36) NOT NULL DEFAULT '' COMMENT '系统编码',
  `sort_num` int(4) NOT NULL DEFAULT '0' COMMENT '显示顺序',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志 0-正常;1-删除',
  `create_by` varchar(64) NOT NULL DEFAULT '' COMMENT '创建者',
  `update_by` varchar(64) NOT NULL DEFAULT '' COMMENT '最后修改者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_sys_code` (`sys_code`) USING BTREE,
  KEY `idx_user_id_sys_code` (`user_id`,`sys_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='用户与目标系统关系表';

-- ----------------------------
-- Records of sso_user_system
-- ----------------------------
BEGIN;
INSERT INTO `sso_user_system` VALUES (1, 1001, 'auth-center', 0, 0, '认证中心管理员', '认证中心管理员', '2021-02-10 18:04:01', '2021-02-10 18:04:07');
INSERT INTO `sso_user_system` VALUES (3, 1002, 'auth-center', 0, 0, 'admin', 'admin', '2021-02-10 18:06:29', '2021-02-10 18:06:29');
INSERT INTO `sso_user_system` VALUES (4, 1003, 'auth-center', 0, 0, 'admin', 'admin', '2021-02-10 18:07:21', '2021-02-10 18:07:21');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
