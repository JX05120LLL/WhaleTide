-- 为attribute_id字段添加默认值(0)
ALTER TABLE pms_products MODIFY COLUMN attribute_id INT DEFAULT 0; 