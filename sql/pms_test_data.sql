-- 品牌表测试数据
INSERT INTO `pms_brands` (`name`, `logo`, `description`, `first_letter`, `sort`, `is_featured`, `status`) VALUES 
('小米', '/uploads/brands/xiaomi.png', '小米科技有限责任公司成立于2010年4月，是一家专注于高端智能手机、智能硬件和IoT生态链建设的创新型科技企业。', 'X', 1, 1, 1),
('华为', '/uploads/brands/huawei.png', '华为技术有限公司成立于1987年，是全球领先的ICT（信息与通信）基础设施和智能终端提供商。', 'H', 2, 1, 1),
('苹果', '/uploads/brands/apple.png', '苹果公司是美国的一家高科技公司，2007年由苹果电脑公司更名而来，是世界领先的科技公司之一。', 'A', 3, 1, 1),
('三星', '/uploads/brands/samsung.png', '三星集团是韩国最大的跨国企业集团，三星电子是三星集团的旗舰子公司。', 'S', 4, 0, 1),
('戴尔', '/uploads/brands/dell.png', '戴尔科技集团是一家总部位于美国德克萨斯州朗德罗克的美国跨国科技公司。', 'D', 5, 0, 1),
('联想', '/uploads/brands/lenovo.png', '联想集团是一家跨国科技公司，总部位于中国北京和美国北卡罗来纳州。', 'L', 6, 0, 1);

-- 商品分类表测试数据（注意：已有默认分类）
INSERT INTO `pms_product_categories` (`parent_id`, `name`, `level`, `icon`, `sort`, `description`, `keywords`, `status`, `is_featured`) VALUES 
(0, '电子产品', 1, '/uploads/categories/electronics.png', 1, '各类电子产品', '电子,数码,智能', 1, 1),
(0, '家用电器', 1, '/uploads/categories/appliances.png', 2, '家用电器产品', '电器,家电,厨房电器', 1, 1),
(0, '服装鞋包', 1, '/uploads/categories/clothing.png', 3, '服装鞋包产品', '服装,鞋子,包包', 1, 0),
(1, '智能手机', 2, '/uploads/categories/smartphone.png', 1, '智能手机产品', '手机,智能手机,5G', 1, 1),
(1, '笔记本电脑', 2, '/uploads/categories/laptop.png', 2, '笔记本电脑产品', '笔记本,电脑,便携', 1, 1),
(1, '平板电脑', 2, '/uploads/categories/tablet.png', 3, '平板电脑产品', '平板,iPad,触屏', 1, 0),
(2, '冰箱', 2, '/uploads/categories/refrigerator.png', 1, '冰箱产品', '冰箱,冷藏,保鲜', 1, 0),
(2, '洗衣机', 2, '/uploads/categories/washing_machine.png', 2, '洗衣机产品', '洗衣机,洗护,滚筒', 1, 0);

-- 商品属性表测试数据
INSERT INTO `pms_product_attributes` (`category_id`, `name`, `attribute_type`, `input_type`, `input_options`, `sort`, `filter_type`, `search_type`, `is_required`) VALUES 
(4, '屏幕尺寸', 0, 1, '5.5英寸,6.1英寸,6.7英寸,7.0英寸', 1, 1, 1, 1),
(4, '内存容量', 0, 1, '4GB,6GB,8GB,12GB,16GB', 2, 1, 1, 1),
(4, '存储容量', 0, 1, '64GB,128GB,256GB,512GB,1TB', 3, 1, 1, 1),
(4, '电池容量', 1, 0, NULL, 4, 0, 0, 0),
(4, '处理器', 1, 0, NULL, 5, 0, 1, 0),
(4, '摄像头像素', 1, 0, NULL, 6, 0, 0, 0),
(5, '屏幕尺寸', 0, 1, '13.3英寸,14英寸,15.6英寸,16英寸', 1, 1, 1, 1),
(5, '处理器', 0, 1, 'Intel i5,Intel i7,Intel i9,AMD Ryzen 5,AMD Ryzen 7', 2, 1, 1, 1),
(5, '内存容量', 0, 1, '8GB,16GB,32GB,64GB', 3, 1, 1, 1),
(5, '固态硬盘', 0, 1, '256GB,512GB,1TB,2TB', 4, 1, 1, 1);

-- 商品信息表测试数据
INSERT INTO `pms_products` (`name`, `product_sn`, `category_id`, `brand_id`, `price`, `original_price`, `main_image`, `keywords`, `brief`, `sale`, `stock`, `unit`, `weight`, `sort`, `publish_status`, `new_status`, `recommend_status`, `verify_status`) VALUES 
('小米13', 'MI13-2023', 4, 1, 3999.00, 4299.00, '/uploads/products/mi13.png', '小米,手机,5G,骁龙8', '小米年度旗舰手机，搭载骁龙8第二代处理器', 1258, 1000, '台', 0.20, 1, 1, 1, 1, 1),
('华为Mate60 Pro', 'HW-MATE60P', 4, 2, 6999.00, 7299.00, '/uploads/products/mate60pro.png', '华为,手机,旗舰,麒麟9000', '华为年度旗舰手机，搭载麒麟9000S处理器', 2056, 500, '台', 0.23, 2, 1, 1, 1, 1),
('iPhone 15 Pro', 'APPL-IP15P', 4, 3, 7999.00, 8299.00, '/uploads/products/iphone15pro.png', 'iPhone,苹果,A17,Pro', 'Apple新一代旗舰手机，搭载A17 Pro芯片', 3102, 800, '台', 0.22, 3, 1, 1, 1, 1),
('联想ThinkPad X1 Carbon', 'LN-X1C2023', 5, 6, 9999.00, 12999.00, '/uploads/products/thinkpadx1.png', '联想,ThinkPad,笔记本,轻薄', '联想商务旗舰笔记本，轻薄坚固', 458, 200, '台', 1.20, 4, 1, 1, 1, 1),
('戴尔XPS 15', 'DELL-XPS15', 5, 5, 12999.00, 13999.00, '/uploads/products/dellxps15.png', '戴尔,XPS,笔记本,设计', '戴尔高性能创意设计笔记本', 325, 150, '台', 1.80, 5, 1, 1, 1, 1);

-- 商品SKU表测试数据
INSERT INTO `pms_product_skus` (`product_id`, `sku_code`, `price`, `stock`, `low_stock`, `image`, `specs`, `specs_text`, `volume`, `weight`, `sale`) VALUES 
(1, 'MI13-8-128-BLACK', 3999.00, 300, 50, '/uploads/skus/mi13-black.png', '{"颜色": "黑色", "内存": "8GB", "存储": "128GB"}', '黑色 8GB+128GB', 0.001, 0.20, 520),
(1, 'MI13-8-256-BLACK', 4299.00, 200, 30, '/uploads/skus/mi13-black.png', '{"颜色": "黑色", "内存": "8GB", "存储": "256GB"}', '黑色 8GB+256GB', 0.001, 0.20, 350),
(1, 'MI13-12-256-WHITE', 4599.00, 300, 50, '/uploads/skus/mi13-white.png', '{"颜色": "白色", "内存": "12GB", "存储": "256GB"}', '白色 12GB+256GB', 0.001, 0.20, 388),
(1, 'MI13-12-512-WHITE', 5299.00, 200, 30, '/uploads/skus/mi13-white.png', '{"颜色": "白色", "内存": "12GB", "存储": "512GB"}', '白色 12GB+512GB', 0.001, 0.20, 0),
(2, 'MATE60P-8-256-BLACK', 6999.00, 100, 20, '/uploads/skus/mate60p-black.png', '{"颜色": "黑色", "内存": "8GB", "存储": "256GB"}', '黑色 8GB+256GB', 0.001, 0.23, 800),
(2, 'MATE60P-12-512-BLACK', 7999.00, 200, 30, '/uploads/skus/mate60p-black.png', '{"颜色": "黑色", "内存": "12GB", "存储": "512GB"}', '黑色 12GB+512GB', 0.001, 0.23, 600),
(2, 'MATE60P-12-512-GREEN', 7999.00, 100, 20, '/uploads/skus/mate60p-green.png', '{"颜色": "绿色", "内存": "12GB", "存储": "512GB"}', '绿色 12GB+512GB', 0.001, 0.23, 656),
(2, 'MATE60P-12-1TB-GREEN', 8999.00, 100, 20, '/uploads/skus/mate60p-green.png', '{"颜色": "绿色", "内存": "12GB", "存储": "1TB"}', '绿色 12GB+1TB', 0.001, 0.23, 0),
(3, 'IP15P-8-256-BLACK', 7999.00, 200, 30, '/uploads/skus/iphone15p-black.png', '{"颜色": "黑色", "内存": "8GB", "存储": "256GB"}', '黑色 8GB+256GB', 0.001, 0.22, 1200),
(3, 'IP15P-8-512-BLACK', 8999.00, 200, 30, '/uploads/skus/iphone15p-black.png', '{"颜色": "黑色", "内存": "8GB", "存储": "512GB"}', '黑色 8GB+512GB', 0.001, 0.22, 800),
(3, 'IP15P-8-1TB-BLACK', 9999.00, 200, 30, '/uploads/skus/iphone15p-black.png', '{"颜色": "黑色", "内存": "8GB", "存储": "1TB"}', '黑色 8GB+1TB', 0.001, 0.22, 1102),
(3, 'IP15P-8-256-SILVER', 7999.00, 200, 30, '/uploads/skus/iphone15p-silver.png', '{"颜色": "银色", "内存": "8GB", "存储": "256GB"}', '银色 8GB+256GB', 0.001, 0.22, 0),
(4, 'X1C-I7-16-512', 9999.00, 50, 10, '/uploads/skus/thinkpadx1.png', '{"处理器": "Intel i7", "内存": "16GB", "固态": "512GB"}', 'Intel i7 16GB+512GB', 0.002, 1.20, 200),
(4, 'X1C-I7-16-1TB', 10999.00, 50, 10, '/uploads/skus/thinkpadx1.png', '{"处理器": "Intel i7", "内存": "16GB", "固态": "1TB"}', 'Intel i7 16GB+1TB', 0.002, 1.20, 158),
(4, 'X1C-I7-32-1TB', 12999.00, 50, 10, '/uploads/skus/thinkpadx1.png', '{"处理器": "Intel i7", "内存": "32GB", "固态": "1TB"}', 'Intel i7 32GB+1TB', 0.002, 1.20, 100),
(5, 'XPS15-I7-16-512', 12999.00, 50, 10, '/uploads/skus/dellxps15.png', '{"处理器": "Intel i7", "内存": "16GB", "固态": "512GB"}', 'Intel i7 16GB+512GB', 0.003, 1.80, 150),
(5, 'XPS15-I9-32-1TB', 15999.00, 50, 10, '/uploads/skus/dellxps15.png', '{"处理器": "Intel i9", "内存": "32GB", "固态": "1TB"}', 'Intel i9 32GB+1TB', 0.003, 1.80, 175);

-- 商品属性值表测试数据
INSERT INTO `pms_product_attribute_values` (`product_id`, `attribute_id`, `value`) VALUES 
(1, 1, '6.7英寸'), 
(1, 2, '8GB,12GB'), 
(1, 3, '128GB,256GB,512GB'), 
(1, 4, '4500mAh'), 
(1, 5, '骁龙8 Gen 2'), 
(1, 6, '5000万像素'), 
(2, 1, '6.7英寸'), 
(2, 2, '8GB,12GB'), 
(2, 3, '256GB,512GB,1TB'), 
(2, 4, '5000mAh'), 
(2, 5, '麒麟9000S'), 
(2, 6, '5000万像素'), 
(3, 1, '6.1英寸'), 
(3, 2, '8GB'), 
(3, 3, '256GB,512GB,1TB'), 
(3, 4, '4400mAh'), 
(3, 5, 'A17 Pro'), 
(3, 6, '4800万像素'), 
(4, 7, '14英寸'), 
(4, 8, 'Intel i7'), 
(4, 9, '16GB,32GB'), 
(4, 10, '512GB,1TB'), 
(5, 7, '15.6英寸'), 
(5, 8, 'Intel i7,Intel i9'), 
(5, 9, '16GB,32GB'), 
(5, 10, '512GB,1TB');

-- 商品详情表测试数据
INSERT INTO `pms_product_details` (`product_id`, `description`, `specs`, `packing_list`, `after_service`) VALUES 
(1, '<p>小米13是小米公司2023年推出的旗舰手机，采用高通骁龙8 Gen 2处理器，提供强大的性能和高效能。6.7英寸AMOLED屏幕，2K分辨率，120Hz刷新率，带来震撼视觉体验。</p><p>搭载徕卡认证的相机系统，主摄像头5000万像素，支持8K视频录制。采用4500mAh大电池，支持120W有线快充和50W无线快充。</p>', '屏幕：6.7英寸AMOLED，2K分辨率，120Hz刷新率\n处理器：高通骁龙8 Gen 2\n内存：8GB/12GB LPDDR5X\n存储：128GB/256GB/512GB UFS 4.0\n电池：4500mAh，支持120W有线快充，50W无线快充\n相机：后置5000万像素主摄+5000万像素超广角+1000万像素长焦，前置3200万像素\n系统：MIUI 14，基于Android 13', '手机主机 x1\n电源适配器 x1\n数据线 x1\n透明保护壳 x1\n说明书 x1\n保修卡 x1\n取卡针 x1', '标准保修1年\n15天无理由退换\n电池终身质保\n免费贴膜服务'),
(2, '<p>华为Mate60 Pro是华为2023年推出的旗舰手机，搭载华为自研的麒麟9000S处理器，采用创新的卫星通信技术，可在无信号区域实现短信通讯。</p><p>采用6.7英寸OLED曲面屏，120Hz刷新率，支持超声波屏下指纹识别。搭载XMAGE影像系统，主摄像头5000万像素，具有出色的暗光拍摄能力。</p>', '屏幕：6.7英寸OLED曲面屏，120Hz刷新率\n处理器：麒麟9000S\n内存：8GB/12GB\n存储：256GB/512GB/1TB\n电池：5000mAh，支持66W有线快充，50W无线快充\n相机：后置5000万像素主摄+1300万像素超广角+1200万像素长焦，前置1300万像素\n系统：HarmonyOS 4.0\n特色：卫星通信，IP68防水', '手机主机 x1\n充电器 x1\n数据线 x1\n透明保护壳 x1\n说明书 x1\n保修卡 x1\n取卡针 x1', '标准保修2年\n7天无理由退换\n电池质保2年\n碎屏险半价更换服务'),
(3, '<p>iPhone 15 Pro是Apple公司2023年推出的旗舰手机，首次采用钛金属材质机身，更轻更坚固。搭载A17 Pro处理器，3nm工艺，性能强劲，能效卓越。</p><p>6.1英寸Super Retina XDR显示屏，支持Always-On显示和ProMotion自适应120Hz刷新率。相机系统升级，4800万像素主摄，支持4K 120fps视频录制和杜比视界格式。</p>', '屏幕：6.1英寸Super Retina XDR，120Hz刷新率\n处理器：A17 Pro\n内存：8GB\n存储：256GB/512GB/1TB\n电池：4400mAh，支持MagSafe无线充电\n相机：后置4800万像素主摄+1200万像素超广角+1200万像素长焦，前置1200万像素\n系统：iOS 17\n特色：钛金属机身，USB-C接口，IP68防水', 'iPhone 15 Pro主机 x1\nUSB-C数据线 x1\n说明书和保修卡 x1\nApple贴纸 x1\n取卡针 x1', '1年有限保修\n可选购AppleCare+延长保修\n14天无理由退换\n原厂配件更换服务'),
(4, '<p>ThinkPad X1 Carbon是联想旗下高端商务笔记本电脑，采用轻薄碳纤维材质机身，重量仅1.2kg，便于携带。</p><p>14英寸2.8K分辨率OLED显示屏，支持杜比视界，色彩准确，适合商务和创意工作。配备英特尔第12代i7处理器，性能强劲，同时保持出色的续航能力。</p>', '屏幕：14英寸2.8K OLED显示屏\n处理器：英特尔第12代i7-1260P\n内存：16GB/32GB LPDDR5\n存储：512GB/1TB SSD\n电池：57Wh，支持快充\n重量：1.2kg\n接口：2×Thunderbolt 4，2×USB 3.2，HDMI 2.0，耳机插孔\n系统：Windows 11专业版', '笔记本电脑主机 x1\n电源适配器 x1\n快速入门指南 x1\n保修卡 x1', '3年上门保修服务\n意外保护服务（可选）\n电池1年保修\n7×24小时技术支持'),
(5, '<p>Dell XPS 15是戴尔旗下的高性能创意设计笔记本电脑，采用铝合金一体成型工艺，搭配碳纤维掌托，兼具质感和耐用性。</p><p>15.6英寸4K+ InfinityEdge触摸屏，100% Adobe RGB色域，适合专业内容创作。搭载英特尔第13代i7/i9处理器和NVIDIA GeForce RTX 4070显卡，提供强大的计算和图形处理能力。</p>', '屏幕：15.6英寸4K+ (3840×2400) InfinityEdge触摸屏\n处理器：英特尔第13代i7-13700H/i9-13900H\n显卡：NVIDIA GeForce RTX 4070\n内存：16GB/32GB DDR5\n存储：512GB/1TB/2TB PCIe SSD\n电池：86Wh\n重量：1.8kg\n接口：2×Thunderbolt 4，1×USB-C 3.2，SD卡槽，耳机插孔\n系统：Windows 11家庭版', 'XPS 15笔记本电脑 x1\n130W USB-C电源适配器 x1\nUSB-C至USB-A转接头 x1\n快速入门指南 x1\n保修信息 x1', '1年标准保修\nPremium Support Plus服务（可选）\n意外损坏保护（可选）\n电池1年保修');

-- 商品图片表测试数据
INSERT INTO `pms_product_images` (`product_id`, `image_url`, `sort`, `is_main`) VALUES 
(1, '/uploads/products/mi13/main.png', 0, 1),
(1, '/uploads/products/mi13/back.png', 1, 0),
(1, '/uploads/products/mi13/side.png', 2, 0),
(1, '/uploads/products/mi13/camera.png', 3, 0),
(1, '/uploads/products/mi13/detail1.png', 4, 0),
(2, '/uploads/products/mate60p/main.png', 0, 1),
(2, '/uploads/products/mate60p/back.png', 1, 0),
(2, '/uploads/products/mate60p/side.png', 2, 0),
(2, '/uploads/products/mate60p/camera.png', 3, 0),
(2, '/uploads/products/mate60p/detail1.png', 4, 0),
(3, '/uploads/products/iphone15p/main.png', 0, 1),
(3, '/uploads/products/iphone15p/back.png', 1, 0),
(3, '/uploads/products/iphone15p/side.png', 2, 0),
(3, '/uploads/products/iphone15p/camera.png', 3, 0),
(3, '/uploads/products/iphone15p/detail1.png', 4, 0),
(4, '/uploads/products/thinkpadx1/main.png', 0, 1),
(4, '/uploads/products/thinkpadx1/side.png', 1, 0),
(4, '/uploads/products/thinkpadx1/keyboard.png', 2, 0),
(4, '/uploads/products/thinkpadx1/screen.png', 3, 0),
(5, '/uploads/products/dellxps15/main.png', 0, 1),
(5, '/uploads/products/dellxps15/side.png', 1, 0),
(5, '/uploads/products/dellxps15/keyboard.png', 2, 0),
(5, '/uploads/products/dellxps15/screen.png', 3, 0);

-- 商品评论表测试数据
INSERT INTO `pms_product_comments` (`user_id`, `product_id`, `order_id`, `order_item_id`, `title`, `content`, `rating`, `images`, `is_anonymous`, `is_show`) VALUES 
(1, 1, 1000001, 10001, '非常满意的购物体验', '收到小米13后非常惊喜，外观设计很精美，性能也非常强大，特别是相机的拍照效果超出预期，夜景模式很惊艳。电池续航也不错，一天重度使用没问题。总的来说是一款值得推荐的旗舰手机！', 5, '/uploads/comments/1001_1.jpg,/uploads/comments/1001_2.jpg', 0, 1),
(2, 1, 1000002, 10002, '性价比很高的手机', '作为小米的忠实用户，这次的小米13没有让我失望。骁龙8Gen2处理器很快，日常使用很流畅，游戏也不卡顿。就是充电有点发热，其他都挺好的。', 4, '/uploads/comments/1002_1.jpg', 0, 1),
(3, 2, 1000003, 10003, '华为Mate60Pro超出预期', '等了很久终于买到了，卫星通信功能确实很实用，出差去偏远地区也不担心没信号了。麒麟芯片表现出色，系统流畅度很高。就是价格有点贵，但值得。', 5, '/uploads/comments/1003_1.jpg,/uploads/comments/1003_2.jpg,/uploads/comments/1003_3.jpg', 0, 1),
(4, 3, 1000004, 10004, 'iPhone15Pro很惊艳', '从安卓转过来的用户，适应了一段时间。不得不说iPhone的系统确实流畅，拍照也很出色，尤其是人像模式。钛金属边框手感很好，比以前的不锈钢轻了不少。', 5, '/uploads/comments/1004_1.jpg', 0, 1),
(5, 3, 1000005, 10005, '还不错，但有些小问题', 'iOS系统流畅度确实没话说，但刚开始用还不太习惯。电池续航一般，重度使用撑不到一天。USB-C接口是个很大的进步，终于可以和其他设备共用充电器了。', 4, NULL, 1, 1),
(6, 4, 1000006, 10006, '商务人士的最佳选择', 'ThinkPad一直是我的最爱，这款X1 Carbon不负期望。键盘手感极佳，轻薄便携，性能足够日常办公和轻度设计使用。指纹识别很快，摄像头也有物理开关，安全性高。', 5, '/uploads/comments/1006_1.jpg', 0, 1),
(7, 5, 1000007, 10007, '创意工作者的得力助手', 'XPS15屏幕素质真的太棒了，色彩准确度高，做设计完全不需要外接显示器。性能也很强劲，运行大型设计软件毫无压力。唯一缺点是风扇噪音有点大，但考虑到性能这是可以接受的。', 4, '/uploads/comments/1007_1.jpg,/uploads/comments/1007_2.jpg', 0, 1);

-- 商品评分表测试数据
INSERT INTO `pms_ratings` (`user_id`, `product_id`, `rating`, `rating_type`) VALUES 
(1, 1, 5, 0), -- 综合评分
(1, 1, 4, 1), -- 服务评分
(1, 1, 5, 2), -- 质量评分
(1, 1, 5, 3), -- 物流评分
(2, 1, 4, 0),
(2, 1, 5, 1),
(2, 1, 4, 2),
(2, 1, 3, 3),
(3, 2, 5, 0),
(3, 2, 5, 1),
(3, 2, 5, 2),
(3, 2, 4, 3),
(4, 3, 5, 0),
(4, 3, 4, 1),
(4, 3, 5, 2),
(4, 3, 5, 3),
(5, 3, 4, 0),
(5, 3, 3, 1),
(5, 3, 5, 2),
(5, 3, 4, 3),
(6, 4, 5, 0),
(6, 4, 5, 1),
(6, 4, 5, 2),
(6, 4, 4, 3),
(7, 5, 4, 0),
(7, 5, 4, 1),
(7, 5, 5, 2),
(7, 5, 3, 3);

-- 库存变更日志表测试数据
INSERT INTO `pms_stock_logs` (`product_id`, `sku_id`, `change_type`, `change_amount`, `before_stock`, `after_stock`, `order_id`, `order_item_id`, `operator`, `remark`) VALUES 
(1, 1, 1, -2, 302, 300, 1000001, 10001, 'system', '订单扣减库存'),
(1, 2, 1, -1, 201, 200, 1000002, 10002, 'system', '订单扣减库存'),
(1, 3, 3, 50, 250, 300, NULL, NULL, 'admin', '后台库存调整'),
(2, 5, 1, -3, 103, 100, 1000003, 10003, 'system', '订单扣减库存'),
(2, 6, 3, 100, 100, 200, NULL, NULL, 'admin', '补充库存'),
(3, 9, 1, -2, 202, 200, 1000004, 10004, 'system', '订单扣减库存'),
(3, 10, 1, -1, 201, 200, 1000005, 10005, 'system', '订单扣减库存'),
(3, 11, 2, 5, 195, 200, 1000008, 10008, 'system', '订单退货增加库存'),
(4, 13, 1, -1, 51, 50, 1000006, 10006, 'system', '订单扣减库存'),
(5, 16, 1, -2, 52, 50, 1000007, 10007, 'system', '订单扣减库存'); 