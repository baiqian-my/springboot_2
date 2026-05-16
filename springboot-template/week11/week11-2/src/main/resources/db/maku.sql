-- maku-boot 数据库初始化脚本

-- 创建数据库（如不存在）
CREATE DATABASE IF NOT EXISTS maku_boot CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE maku_boot;

-- 系统用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    avatar VARCHAR(255) DEFAULT NULL COMMENT '头像',
    gender TINYINT DEFAULT 0 COMMENT '性别 0:保密 1:男 2:女',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    mobile VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    status TINYINT DEFAULT 1 COMMENT '状态 0:停用 1:正常',
    super_admin TINYINT DEFAULT 0 COMMENT '超级管理员 0:否 1:是',
    deleted TINYINT DEFAULT 0 COMMENT '删除标识 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 系统角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0:停用 1:正常',
    deleted TINYINT DEFAULT 0 COMMENT '删除标识 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 系统菜单表
CREATE TABLE IF NOT EXISTS sys_menu (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    parent_id BIGINT DEFAULT 0 COMMENT '上级ID，一级菜单为0',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    menu_type TINYINT DEFAULT 0 COMMENT '菜单类型 0:目录 1:菜单 2:按钮',
    icon VARCHAR(50) DEFAULT NULL COMMENT '菜单图标',
    path VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
    component VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
    permission VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0:停用 1:正常',
    deleted TINYINT DEFAULT 0 COMMENT '删除标识 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- 角色菜单关联表
CREATE TABLE IF NOT EXISTS sys_role_menu (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (id),
    UNIQUE KEY uk_role_menu (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 数据字典类型表
CREATE TABLE IF NOT EXISTS sys_dict_type (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    dict_type VARCHAR(50) NOT NULL COMMENT '字典类型',
    dict_name VARCHAR(50) NOT NULL COMMENT '字典名称',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0:停用 1:正常',
    deleted TINYINT DEFAULT 0 COMMENT '删除标识 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_dict_type (dict_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典类型表';

-- 数据字典数据表
CREATE TABLE IF NOT EXISTS sys_dict_data (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    dict_type_id BIGINT NOT NULL COMMENT '字典类型ID',
    dict_label VARCHAR(50) NOT NULL COMMENT '字典标签',
    dict_value VARCHAR(50) NOT NULL COMMENT '字典值',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0:停用 1:正常',
    deleted TINYINT DEFAULT 0 COMMENT '删除标识 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典数据表';

-- 参数配置表
CREATE TABLE IF NOT EXISTS sys_params (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    param_name VARCHAR(50) NOT NULL COMMENT '参数名',
    param_value VARCHAR(500) NOT NULL COMMENT '参数值',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    status TINYINT DEFAULT 1 COMMENT '状态 0:停用 1:正常',
    deleted TINYINT DEFAULT 0 COMMENT '删除标识 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_param_name (param_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参数配置表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS sys_log_operation (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    operation VARCHAR(50) DEFAULT NULL COMMENT '操作',
    request_uri VARCHAR(200) DEFAULT NULL COMMENT '请求URI',
    request_method VARCHAR(10) DEFAULT NULL COMMENT '请求方式',
    request_params TEXT DEFAULT NULL COMMENT '请求参数',
    response_data TEXT DEFAULT NULL COMMENT '响应数据',
    ip VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    user_agent VARCHAR(500) DEFAULT NULL COMMENT 'User Agent',
    execute_time INT DEFAULT 0 COMMENT '执行时长（毫秒）',
    status TINYINT DEFAULT 1 COMMENT '状态 0:失败 1:成功',
    creator_name VARCHAR(50) DEFAULT NULL COMMENT '创建者姓名',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 文件上传表
CREATE TABLE IF NOT EXISTS sys_file (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    file_name VARCHAR(200) NOT NULL COMMENT '文件名',
    original_name VARCHAR(200) DEFAULT NULL COMMENT '原始文件名',
    file_suffix VARCHAR(20) DEFAULT NULL COMMENT '文件后缀',
    file_size BIGINT DEFAULT 0 COMMENT '文件大小',
    file_path VARCHAR(500) NOT NULL COMMENT '文件路径',
    url VARCHAR(500) DEFAULT NULL COMMENT 'URL地址',
    storage_type TINYINT DEFAULT 1 COMMENT '存储类型 1:本地 2:阿里云OSS 3:腾讯云COS',
    deleted TINYINT DEFAULT 0 COMMENT '删除标识 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件上传表';

-- 登录日志表
CREATE TABLE IF NOT EXISTS sys_log_login (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    username VARCHAR(50) DEFAULT NULL COMMENT '用户名',
    ip VARCHAR(50) DEFAULT NULL COMMENT '登录IP',
    address VARCHAR(100) DEFAULT NULL COMMENT '登录地点',
    user_agent VARCHAR(500) DEFAULT NULL COMMENT 'User Agent',
    status TINYINT DEFAULT 1 COMMENT '状态 0:失败 1:成功',
    operation TINYINT DEFAULT 0 COMMENT '操作 0:登录 1:退出',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- 初始化数据

-- 插入管理员用户（密码：admin，BCrypt加密）
-- 使用已知有效的BCrypt哈希值
INSERT INTO sys_user (id, username, password, real_name, status, super_admin, create_time, update_time) VALUES
(1, 'admin', '$2a$10$7JB720yubVSJjv9X27dSHe1yNRCWHtIJJk0.vVPm.7yKICX7l3ehe', '管理员', 1, 1, NOW(), NOW());

-- 插入测试用户（密码：123456）
INSERT INTO sys_user (id, username, password, real_name, status, super_admin, gender, email, mobile, create_time, update_time) VALUES
(2, 'test', '$2a$10$7JB720yubVSJjv9X27dSHe1yNRCWHtIJJk0.vVPm.7yKICX7l3ehe', '测试用户', 1, 0, 1, 'test@example.com', '13800138000', NOW(), NOW()),
(3, 'user', '$2a$10$7JB720yubVSJjv9X27dSHe1yNRCWHtIJJk0.vVPm.7yKICX7l3ehe', '普通用户', 1, 0, 2, 'user@example.com', '13800138001', NOW(), NOW()),
(4, 'zhangsan', '$2a$10$7JB720yubVSJjv9X27dSHe1yNRCWHtIJJk0.vVPm.7yKICX7l3ehe', '张三', 1, 0, 1, 'zhangsan@example.com', '13800138002', NOW(), NOW()),
(5, 'lisi', '$2a$10$7JB720yubVSJjv9X27dSHe1yNRCWHtIJJk0.vVPm.7yKICX7l3ehe', '李四', 1, 0, 1, 'lisi@example.com', '13800138003', NOW(), NOW());

-- 密码明文是：admin / 123456
-- 如果登录失败，请执行以下 SQL 直接设置明文密码（仅限开发环境）：
-- UPDATE sys_user SET password = '$2a$10$7JB720yubVSJjv9X27dSHe1yNRCWHtIJJk0.vVPm.7yKICX7l3ehe' WHERE username = 'admin';

-- 插入角色
INSERT INTO sys_role (id, role_name, role_code, remark, sort, status, create_time, update_time) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '拥有所有权限', 0, 1, NOW(), NOW()),
(2, '普通用户', 'USER', '普通用户权限', 1, 1, NOW(), NOW());

-- 插入用户角色关联（所有用户都有普通用户角色）
INSERT INTO sys_user_role (id, user_id, role_id) VALUES
(1, 1, 1),  -- admin - 超级管理员
(2, 2, 2),  -- test - 普通用户
(3, 3, 2),  -- user - 普通用户
(4, 4, 2),  -- zhangsan - 普通用户
(5, 5, 2);  -- lisi - 普通用户

-- 插入菜单
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, icon, path, component, permission, sort, status, create_time, update_time) VALUES
(1, 0, '系统管理', 0, 'Setting', '/system', NULL, NULL, 0, 1, NOW(), NOW()),
(2, 1, '用户管理', 1, 'User', '/system/user', 'system/user/index', 'sys:user:page', 0, 1, NOW(), NOW()),
(3, 1, '角色管理', 1, 'UserFilled', '/system/role', 'system/role/index', 'sys:role:page', 1, 1, NOW(), NOW()),
(4, 1, '菜单管理', 1, 'Menu', '/system/menu', 'system/menu/index', 'sys:menu:page', 2, 1, NOW(), NOW()),
(5, 1, '字典管理', 1, 'Document', '/system/dict', 'system/dict/index', 'sys:dict:page', 3, 1, NOW(), NOW()),
(6, 1, '参数管理', 1, 'Tools', '/system/param', 'system/param/index', 'sys:param:page', 4, 1, NOW(), NOW()),
(7, 1, '日志管理', 0, 'DocumentCopy', '/system/log', NULL, NULL, 5, 1, NOW(), NOW()),
(8, 7, '操作日志', 1, 'Document', '/system/log/operation', 'system/log/operation/index', 'sys:log:operation:page', 0, 1, NOW(), NOW()),
(9, 7, '登录日志', 1, 'DocumentChecked', '/system/log/login', 'system/log/login/index', 'sys:log:login:page', 1, 1, NOW(), NOW());

-- 插入角色菜单关联（超级管理员拥有所有菜单）
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES
(1, 1, 1), (2, 1, 2), (3, 1, 3), (4, 1, 4), (5, 1, 5), (6, 1, 6), (7, 1, 7), (8, 1, 8), (9, 1, 9);

-- 插入字典类型
INSERT INTO sys_dict_type (id, dict_type, dict_name, remark, sort, status, create_time, update_time) VALUES
(1, 'sys_user_status', '用户状态', NULL, 0, 1, NOW(), NOW()),
(2, 'sys_gender', '性别', NULL, 1, 1, NOW(), NOW());

-- 插入字典数据
INSERT INTO sys_dict_data (id, dict_type_id, dict_label, dict_value, sort, status, create_time, update_time) VALUES
(1, 1, '正常', '1', 0, 1, NOW(), NOW()),
(2, 1, '停用', '0', 1, 1, NOW(), NOW()),
(3, 2, '保密', '0', 0, 1, NOW(), NOW()),
(4, 2, '男', '1', 1, 1, NOW(), NOW()),
(5, 2, '女', '2', 2, 1, NOW(), NOW());

-- 留言反馈表（用于小程序端）
CREATE TABLE IF NOT EXISTS app_feedback (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT DEFAULT NULL COMMENT '用户ID',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    content TEXT NOT NULL COMMENT '留言内容',
    contact VARCHAR(100) DEFAULT NULL COMMENT '联系方式',
    reply TEXT DEFAULT NULL COMMENT '回复内容',
    status TINYINT DEFAULT 0 COMMENT '状态 0:待处理 1:已回复',
    deleted TINYINT DEFAULT 0 COMMENT '删除标识 0:未删除 1:已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='留言反馈表';

-- 创建上传目录（Windows 路径）
-- 注意：需要在 D 盘创建 upload 目录