-- 为attribute_id字段添加默认值(0)
ALTER TABLE pms_products MODIFY COLUMN attribute_id INT DEFAULT 0;

-- 删除角色菜单关系表中的退货原因设置菜单关联
DELETE FROM `ams_role_menu_relations` WHERE `menu_id` = 35;

-- 删除退货原因设置菜单
DELETE FROM `ams_menus` WHERE `id` = 35; 