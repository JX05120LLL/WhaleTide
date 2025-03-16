-- 清理原有角色及关联数据
DELETE FROM `ams_role_resource_relations`;
DELETE FROM `ams_role_permission_relations`;
DELETE FROM `ams_role_menu_relations`;
DELETE FROM `ams_admin_role_relations`;
DELETE FROM `ams_roles`;

-- 重新创建角色
INSERT INTO `ams_roles` VALUES (1, '超级管理员', 'SUPER_ADMIN', '拥有所有操作权限，可以管理整个系统', 0, 1, 0, NOW(), NOW());
INSERT INTO `ams_roles` VALUES (2, '商家', 'MERCHANT', '拥有商品、订单、库存、营销管理权限', 1, 1, 0, NOW(), NOW());
INSERT INTO `ams_roles` VALUES (3, '普通用户', 'NORMAL_USER', '只具有基本查看权限，无修改权限', 2, 1, 0, NOW(), NOW());

-- 超级管理员菜单权限（拥有全部菜单权限）
INSERT INTO `ams_role_menu_relations` (`role_id`, `menu_id`, `create_time`) VALUES 
(1, 20, NOW()), -- 首页
(1, 21, NOW()), -- 商品
(1, 22, NOW()), -- 订单
(1, 23, NOW()), -- 营销
(1, 24, NOW()), -- 权限
(1, 25, NOW()), -- 仪表盘
(1, 26, NOW()), -- 商品列表
(1, 27, NOW()), -- 添加商品
(1, 28, NOW()), -- 商品分类
(1, 29, NOW()), -- 品牌管理
(1, 30, NOW()), -- 商品类型
(1, 31, NOW()), -- 修改商品
(1, 32, NOW()), -- 订单列表
(1, 33, NOW()), -- 订单设置
(1, 34, NOW()), -- 退货申请处理
(1, 35, NOW()), -- 退货原因设置
(1, 36, NOW()), -- 秒杀活动列表
(1, 37, NOW()), -- 优惠券列表
(1, 38, NOW()), -- 添加优惠券
(1, 39, NOW()), -- 用户列表
(1, 40, NOW()), -- 角色列表
(1, 41, NOW()), -- 菜单列表
(1, 42, NOW()); -- 资源列表

-- 商家菜单权限（商品、订单、营销管理）
INSERT INTO `ams_role_menu_relations` (`role_id`, `menu_id`, `create_time`) VALUES 
(2, 20, NOW()), -- 首页
(2, 21, NOW()), -- 商品
(2, 22, NOW()), -- 订单
(2, 23, NOW()), -- 营销
(2, 25, NOW()), -- 仪表盘
(2, 26, NOW()), -- 商品列表
(2, 27, NOW()), -- 添加商品
(2, 28, NOW()), -- 商品分类
(2, 29, NOW()), -- 品牌管理
(2, 30, NOW()), -- 商品类型
(2, 31, NOW()), -- 修改商品
(2, 32, NOW()), -- 订单列表
(2, 33, NOW()), -- 订单设置
(2, 34, NOW()), -- 退货申请处理
(2, 36, NOW()), -- 秒杀活动列表
(2, 37, NOW()); -- 优惠券列表

-- 普通用户菜单权限（只有基本查看权限）
INSERT INTO `ams_role_menu_relations` (`role_id`, `menu_id`, `create_time`) VALUES 
(3, 20, NOW()), -- 首页
(3, 21, NOW()), -- 商品
(3, 22, NOW()), -- 订单
(3, 25, NOW()), -- 仪表盘
(3, 26, NOW()), -- 商品列表
(3, 32, NOW()); -- 订单列表

-- 超级管理员资源权限（拥有全部API访问权限）
INSERT INTO `ams_role_resource_relations` (`role_id`, `resource_id`, `create_time`) VALUES 
(1, 11, NOW()), -- 商品列表
(1, 12, NOW()), -- 添加商品
(1, 13, NOW()), -- 修改商品
(1, 14, NOW()), -- 删除商品
(1, 15, NOW()), -- 商品详情
(1, 16, NOW()), -- 商品分类列表
(1, 17, NOW()), -- 品牌列表
(1, 18, NOW()), -- 订单列表
(1, 19, NOW()), -- 订单详情
(1, 20, NOW()), -- 订单发货
(1, 21, NOW()), -- 关闭订单
(1, 22, NOW()), -- 订单设置
(1, 23, NOW()), -- 退货申请列表
(1, 24, NOW()), -- 秒杀活动列表
(1, 25, NOW()), -- 添加秒杀活动
(1, 26, NOW()), -- 优惠券列表
(1, 27, NOW()), -- 添加优惠券
(1, 28, NOW()), -- 用户列表
(1, 29, NOW()), -- 添加用户
(1, 30, NOW()), -- 角色列表
(1, 31, NOW()), -- 添加角色
(1, 32, NOW()), -- 菜单列表
(1, 33, NOW()); -- 资源列表

-- 商家资源权限（商品、订单、营销相关API，但无系统管理权限）
INSERT INTO `ams_role_resource_relations` (`role_id`, `resource_id`, `create_time`) VALUES 
(2, 11, NOW()), -- 商品列表
(2, 12, NOW()), -- 添加商品
(2, 13, NOW()), -- 修改商品
(2, 14, NOW()), -- 删除商品
(2, 15, NOW()), -- 商品详情
(2, 16, NOW()), -- 商品分类列表
(2, 17, NOW()), -- 品牌列表
(2, 18, NOW()), -- 订单列表
(2, 19, NOW()), -- 订单详情
(2, 20, NOW()), -- 订单发货
(2, 21, NOW()), -- 关闭订单
(2, 22, NOW()), -- 订单设置
(2, 23, NOW()), -- 退货申请列表
(2, 24, NOW()), -- 秒杀活动列表
(2, 25, NOW()), -- 添加秒杀活动
(2, 26, NOW()), -- 优惠券列表
(2, 27, NOW()); -- 添加优惠券

-- 普通用户资源权限（只有查看权限，无修改权限）
INSERT INTO `ams_role_resource_relations` (`role_id`, `resource_id`, `create_time`) VALUES 
(3, 11, NOW()), -- 商品列表
(3, 15, NOW()), -- 商品详情
(3, 16, NOW()), -- 商品分类列表
(3, 17, NOW()), -- 品牌列表
(3, 18, NOW()), -- 订单列表
(3, 19, NOW()), -- 订单详情
(3, 24, NOW()), -- 秒杀活动列表
(3, 26, NOW()); -- 优惠券列表

-- 创建默认管理员账号（如果需要）
-- 如果ams_admins表中已有超级管理员，请跳过此步骤或根据实际情况修改
INSERT INTO `ams_admins` (`username`, `password`, `icon`, `email`, `nick_name`, `note`, `status`) 
SELECT 'admin', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', NULL, 'admin@example.com', '系统管理员', '超级管理员账号', 1
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `ams_admins` WHERE `username` = 'admin');

-- 为默认管理员分配超级管理员角色
INSERT INTO `ams_admin_role_relations` (`admin_id`, `role_id`, `create_time`)
SELECT (SELECT id FROM `ams_admins` WHERE `username` = 'admin'), 1, NOW()
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM `ams_admin_role_relations` 
  WHERE `admin_id` = (SELECT id FROM `ams_admins` WHERE `username` = 'admin')
  AND `role_id` = 1
); 