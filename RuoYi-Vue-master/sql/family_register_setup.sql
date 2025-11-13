-- 家庭财务管理系统注册功能设置脚本
-- 创建时间: 2025-01-01

-- 1. 启用用户注册功能
INSERT INTO sys_config (config_name, config_key, config_value, config_type, create_by, create_time, update_by, update_time, remark) 
VALUES ('账户自助-是否开启用户注册功能', 'sys.account.registerUser', 'true', 'Y', 'admin', NOW(), 'admin', NOW(), '是否开启注册用户功能（true开启，false关闭）')
ON DUPLICATE KEY UPDATE config_value = 'true', update_time = NOW();

-- 2. 创建家庭管理员角色
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES (100, '家庭管理员', 'family_admin', 3, '1', 1, 1, '0', '0', 'admin', NOW(), 'admin', NOW(), '家庭管理员角色，拥有家庭财务管理的所有权限')
ON DUPLICATE KEY UPDATE role_name = '家庭管理员', role_key = 'family_admin', remark = '家庭管理员角色，拥有家庭财务管理的所有权限';

-- 3. 创建普通成员角色
INSERT INTO sys_role (role_id, role_name, role_key, role_sort, data_scope, menu_check_strictly, dept_check_strictly, status, del_flag, create_by, create_time, update_by, update_time, remark)
VALUES (101, '家庭成员', 'family_member', 4, '2', 1, 1, '0', '0', 'admin', NOW(), 'admin', NOW(), '普通家庭成员角色，具有基本的查看和记录权限')
ON DUPLICATE KEY UPDATE role_name = '家庭成员', role_key = 'family_member', remark = '普通家庭成员角色，具有基本的查看和记录权限';

-- 4. 查询现有菜单权限，为角色分配权限（家庭管理员 - 所有家庭相关权限）
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 100, menu_id FROM sys_menu WHERE menu_name LIKE '%家庭%' OR menu_name LIKE '%财务%' OR menu_name LIKE '%收支%' OR menu_name LIKE '%管家%' OR path LIKE '/family%' OR path LIKE '/manager%' OR path LIKE '/familyinfo%';

-- 5. 为家庭成员角色分配基础权限（查看和基本操作权限）
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 101, menu_id FROM sys_menu WHERE (menu_name LIKE '%财务%' OR menu_name LIKE '%收支%' OR path LIKE '/family%' OR path LIKE '/manager%') 
AND menu_name NOT LIKE '%删除%' AND menu_name NOT LIKE '%导出%' AND perms NOT LIKE '%:remove%' AND perms NOT LIKE '%:export%';

-- 6. 确保角色状态正常
UPDATE sys_role SET status = '0' WHERE role_id IN (100, 101);

-- 查看结果
SELECT * FROM sys_config WHERE config_key = 'sys.account.registerUser';
SELECT * FROM sys_role WHERE role_id IN (100, 101);
