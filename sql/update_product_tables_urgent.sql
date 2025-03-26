-- 添加报错中提到的所有缺失字段
ALTER TABLE `pms_products`
    ADD COLUMN `album_pics` varchar(1000) DEFAULT NULL COMMENT '画册图片，连产品图片限制为5张，以逗号分割' AFTER `main_image`,
    ADD COLUMN `detail_title` varchar(255) DEFAULT NULL COMMENT '商品详情标题' AFTER `album_pics`,
    ADD COLUMN `detail_desc` text COMMENT '商品详情描述' AFTER `detail_title`,
    ADD COLUMN `detail_html` text COMMENT 'PC端商品详情' AFTER `detail_desc`,
    ADD COLUMN `detail_mobile_content` text COMMENT '移动端商品详情' AFTER `detail_html`,
    ADD COLUMN `promotion_type` tinyint(4) DEFAULT 0 COMMENT '促销类型：0->没有促销；1->单品促销；2->会员价；3->阶梯价格；4->满减价格' AFTER `detail_mobile_content`,
    ADD COLUMN `promotion_start_time` datetime DEFAULT NULL COMMENT '促销开始时间' AFTER `promotion_type`,
    ADD COLUMN `promotion_end_time` datetime DEFAULT NULL COMMENT '促销结束时间' AFTER `promotion_start_time`,
    ADD COLUMN `promotion_price` decimal(10,2) DEFAULT NULL COMMENT '促销价格' AFTER `promotion_end_time`,
    ADD COLUMN `promotion_per_limit` int(11) DEFAULT NULL COMMENT '活动限购数量' AFTER `promotion_price`,
    ADD COLUMN `new_status` tinyint(4) DEFAULT 0 COMMENT '新品状态:0->不是新品；1->新品' AFTER `publish_status`,
    ADD COLUMN `recommend_status` tinyint(4) DEFAULT 0 COMMENT '推荐状态；0->不推荐；1->推荐' AFTER `new_status`,
    ADD COLUMN `verify_status` tinyint(4) DEFAULT 0 COMMENT '审核状态：0->未审核；1->审核通过' AFTER `recommend_status`,
    ADD COLUMN `is_deleted` tinyint(4) DEFAULT 0 COMMENT '删除状态：0->未删除；1->已删除' AFTER `verify_status`; 