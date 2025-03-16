-- 添加更多品牌表测试数据
INSERT INTO `pms_brands` (`name`, `logo`, `description`, `first_letter`, `sort`, `is_featured`, `status`) VALUES 
('索尼', '/uploads/brands/sony.png', '索尼是一家总部位于日本的跨国公司，提供消费电子、游戏、娱乐和金融服务。', 'S', 7, 1, 1),
('LG', '/uploads/brands/lg.png', 'LG电子是LG集团的一部分，专注于家电和移动通信设备的制造。', 'L', 8, 0, 1),
('松下', '/uploads/brands/panasonic.png', '松下是一家日本的跨国电子制造商，专注于电子设备、家电和工业设备。', 'S', 9, 0, 1),
('飞利浦', '/uploads/brands/philips.png', '飞利浦是一家荷兰的健康科技公司，专注于健康科技和家用电器。', 'F', 10, 1, 1);

-- 添加更多商品分类表测试数据
INSERT INTO `pms_product_categories` (`parent_id`, `name`, `level`, `icon`, `sort`, `description`, `keywords`, `status`, `is_featured`) VALUES 
(1, '智能穿戴', 2, '/uploads/categories/wearable.png', 4, '智能穿戴设备', '智能手表,手环,耳机', 1, 1),
(1, '智能家居', 2, '/uploads/categories/smarthome.png', 5, '智能家居产品', '智能音箱,智能灯泡,智能插座', 1, 1),
(1, '相机', 2, '/uploads/categories/camera.png', 6, '相机产品', '相机,单反,微单', 1, 0),
(2, '电视', 2, '/uploads/categories/tv.png', 3, '电视产品', '电视,智能电视,OLED', 1, 1),
(2, '空调', 2, '/uploads/categories/aircon.png', 4, '空调产品', '空调,变频,冷暖', 1, 0);

-- 添加更多商品属性表测试数据
INSERT INTO `pms_product_attributes` (`category_id`, `name`, `attribute_type`, `input_type`, `input_options`, `sort`, `filter_type`, `search_type`, `is_required`) VALUES 
(12, '屏幕尺寸', 0, 1, '1.2英寸,1.4英寸,1.6英寸,1.8英寸', 1, 1, 1, 1),
(12, '续航时间', 0, 1, '1天,3天,7天,14天', 2, 1, 1, 1),
(12, '防水等级', 0, 1, 'IP67,IP68,5ATM', 3, 1, 0, 0),
(12, '健康功能', 1, 0, NULL, 4, 0, 1, 0),
(13, '连接方式', 0, 1, 'Wi-Fi,蓝牙,Zigbee,Z-Wave', 1, 1, 1, 1),
(13, '兼容系统', 0, 1, 'iOS,Android,Windows,HomeKit,Alexa,Google Assistant', 2, 1, 1, 1),
(15, '屏幕类型', 0, 1, 'LED,OLED,QLED,Mini LED', 1, 1, 1, 1),
(15, '分辨率', 0, 1, '1080P,4K,8K', 2, 1, 1, 1),
(15, '智能系统', 0, 1, 'Android TV,webOS,VIDAA', 3, 1, 1, 0);

-- 添加更多商品信息表测试数据
INSERT INTO `pms_products` (`name`, `product_sn`, `category_id`, `brand_id`, `price`, `original_price`, `main_image`, `keywords`, `brief`, `sale`, `stock`, `unit`, `weight`, `sort`, `publish_status`, `new_status`, `recommend_status`, `verify_status`) VALUES 
('索尼WH-1000XM5无线降噪耳机', 'SONY-WH1000XM5', 12, 7, 2699.00, 2999.00, '/uploads/products/wh1000xm5.png', '索尼,耳机,降噪,蓝牙', '索尼旗舰级无线降噪耳机，提供出色的降噪体验', 856, 300, '台', 0.25, 6, 1, 1, 1, 1),
('小米手环8 Pro', 'MI-BAND8PRO', 12, 1, 399.00, 459.00, '/uploads/products/miband8pro.png', '小米,手环,健康,运动', '小米全新智能手环，支持运动健康监测', 1520, 1000, '个', 0.03, 7, 1, 1, 1, 1),
('华为智慧屏V75 Super', 'HW-TV-V75S', 15, 2, 12999.00, 13999.00, '/uploads/products/huawei-v75s.png', '华为,电视,智慧屏,大屏', '华为75英寸旗舰智慧屏，搭载鸿蒙系统', 256, 100, '台', 30.00, 8, 1, 1, 1, 1),
('飞利浦Hue智能灯泡套装', 'PHILIPS-HUE-KIT', 13, 10, 799.00, 899.00, '/uploads/products/philips-hue.png', '飞利浦,Hue,灯泡,智能家居', '飞利浦Hue智能照明系统，可通过手机APP控制', 423, 200, '套', 0.50, 9, 1, 1, 1, 1),
('索尼Alpha 7 IV全画幅微单相机', 'SONY-A7IV', 14, 7, 14999.00, 15999.00, '/uploads/products/sony-a7iv.png', '索尼,相机,微单,全画幅', '索尼专业级全画幅微单相机，3300万像素', 189, 50, '台', 0.70, 10, 1, 1, 1, 1);

-- 添加更多商品SKU表测试数据
INSERT INTO `pms_product_skus` (`product_id`, `sku_code`, `price`, `stock`, `low_stock`, `image`, `specs`, `specs_text`, `volume`, `weight`, `sale`) VALUES 
(6, 'WH1000XM5-BLACK', 2699.00, 100, 20, '/uploads/skus/wh1000xm5-black.png', '{"颜色": "黑色"}', '黑色', 0.002, 0.25, 500),
(6, 'WH1000XM5-WHITE', 2699.00, 100, 20, '/uploads/skus/wh1000xm5-white.png', '{"颜色": "白色"}', '白色', 0.002, 0.25, 356),
(6, 'WH1000XM5-BLUE', 2799.00, 100, 20, '/uploads/skus/wh1000xm5-blue.png', '{"颜色": "蓝色"}', '蓝色', 0.002, 0.25, 0),
(7, 'MI-BAND8PRO-BLACK', 399.00, 300, 50, '/uploads/skus/miband8pro-black.png', '{"颜色": "黑色"}', '黑色', 0.0001, 0.03, 600),
(7, 'MI-BAND8PRO-BLUE', 399.00, 300, 50, '/uploads/skus/miband8pro-blue.png', '{"颜色": "蓝色"}', '蓝色', 0.0001, 0.03, 520),
(7, 'MI-BAND8PRO-GREEN', 399.00, 300, 50, '/uploads/skus/miband8pro-green.png', '{"颜色": "绿色"}', '绿色', 0.0001, 0.03, 400),
(7, 'MI-BAND8PRO-RED', 459.00, 100, 20, '/uploads/skus/miband8pro-red.png', '{"颜色": "红色"}', '红色限定版', 0.0001, 0.03, 0),
(8, 'HW-TV-V75S-STD', 12999.00, 50, 10, '/uploads/skus/huawei-v75s.png', '{"版本": "标准版"}', '标准版', 0.5, 30.00, 156),
(8, 'HW-TV-V75S-PRO', 14999.00, 50, 10, '/uploads/skus/huawei-v75s-pro.png', '{"版本": "Pro版"}', 'Pro版', 0.5, 30.50, 100),
(9, 'PHILIPS-HUE-3BULB', 799.00, 100, 20, '/uploads/skus/philips-hue-3.png', '{"套装": "3个灯泡套装"}', '3个灯泡套装', 0.008, 0.50, 250),
(9, 'PHILIPS-HUE-5BULB', 1299.00, 100, 20, '/uploads/skus/philips-hue-5.png', '{"套装": "5个灯泡套装"}', '5个灯泡套装', 0.012, 0.80, 173),
(10, 'SONY-A7IV-BODY', 14999.00, 20, 5, '/uploads/skus/sony-a7iv-body.png', '{"套装": "机身"}', '机身', 0.005, 0.70, 100),
(10, 'SONY-A7IV-KIT', 17999.00, 30, 5, '/uploads/skus/sony-a7iv-kit.png', '{"套装": "标准镜头套装"}', '标准镜头套装', 0.007, 1.20, 89);

-- 添加更多商品属性值表测试数据
INSERT INTO `pms_product_attribute_values` (`product_id`, `attribute_id`, `value`) VALUES 
(6, 11, '1.2英寸'), 
(6, 12, '3天'), 
(6, 13, 'IP67'), 
(6, 14, '心率监测,血氧监测,睡眠监测,压力监测'), 
(7, 11, '1.6英寸'), 
(7, 12, '14天'), 
(7, 13, '5ATM'), 
(7, 14, '心率监测,血氧监测,睡眠监测,压力监测,运动模式110+'), 
(8, 16, 'OLED'), 
(8, 17, '4K'), 
(8, 18, 'HarmonyOS'), 
(9, 14, 'Wi-Fi,蓝牙,Zigbee'), 
(9, 15, 'iOS,Android,HomeKit,Alexa,Google Assistant'), 
(10, 14, 'Wi-Fi,蓝牙');

-- 添加更多商品详情表测试数据
INSERT INTO `pms_product_details` (`product_id`, `description`, `specs`, `packing_list`, `after_service`) VALUES 
(6, '<p>索尼WH-1000XM5无线降噪耳机是索尼最新一代旗舰降噪耳机，采用全新设计，配备8个麦克风和2个处理器，提供业界领先的降噪性能。</p><p>全新驱动单元带来更加纯净的高解析度音质，支持LDAC高解析度无线音频传输，还有DSEE Extreme实时还原音频细节。触控操作方便直观，支持多点连接可同时连接两台设备。</p>', '型号：WH-1000XM5\n佩戴方式：头戴式\n蓝牙版本：5.2\n传输协议：SBC, AAC, LDAC\n续航时间：降噪开启30小时，关闭40小时\n重量：250g\n麦克风：8个（用于降噪和通话）\n降噪性能：行业领先\n充电接口：USB-C\n快速充电：3分钟充电可使用3小时', '耳机 x1\n携带包 x1\n充电线（USB-C） x1\n音频线 x1\n飞机转接头 x1\n说明书 x1', '全国联保1年\n7天无理由退换\n30天保价\n免费软件升级\n预约专业客服解答'),
(7, '<p>小米手环8 Pro采用1.74英寸AMOLED高清大屏，支持常亮显示和自定义表盘，内置多种运动模式，全天候监测心率、血氧、睡眠等健康指标。</p><p>新一代生物传感器提供更准确的健康数据，14天超长续航，5ATM防水可游泳佩戴。支持小爱同学语音助手和支付宝快捷支付功能，是您健康生活的智能助手。</p>', '屏幕：1.74英寸AMOLED\n分辨率：336 x 480\n亮度：600尼特，支持自动亮度调节\n材质：铝合金中框，高强度钢化玻璃\n防水：5ATM专业级防水\n连接：蓝牙5.2 BLE\n电池：容量200mAh，续航14天\n传感器：心率传感器，血氧传感器，加速度传感器，陀螺仪\n支持功能：110+运动模式，24小时健康监测，支付宝支付，小爱同学', '手环主体 x1\n专用充电线 x1\n说明书 x1', '全国联保1年\n7天无理由退换\n运动数据云同步\n固件免费升级\n官方指定维修点保修'),
(8, '<p>华为智慧屏V75 Super采用75英寸超大4K OLED屏幕，搭载鸿蒙HarmonyOS系统，支持全场景智慧互联。画质方面，支持HDR Vivid画质增强，色彩鲜艳逼真，对比度极佳。</p><p>配备华为自研智慧引擎芯片，AI画质优化，运行流畅不卡顿。内置华为智慧音响系统，14个扬声器打造3D环绕立体声。支持智慧语音控制和AI健身教练等特色功能，全方位提升家庭娱乐体验。</p>', '屏幕：75英寸OLED\n分辨率：4K超高清 3840x2160\n刷新率：120Hz\n系统：HarmonyOS\n处理器：鸿鹄智慧芯片\n内存：4GB\n存储：64GB\n音箱：14扬声器3D音响系统\n接口：HDMI x3, USB x2, 网口x1\n无线连接：Wi-Fi 6+, 蓝牙5.1, NFC\n特色功能：一碰投屏, AI健身, 智慧语音, 智慧家居控制中心', '电视主机 x1\n底座 x1\n遥控器 x1\n电源线 x1\n说明书 x1\n壁挂配件 x1', '整机3年保修\n主要部件5年保修\n上门安装调试\n24小时智能客服\n软件终身免费升级'),
(9, '<p>飞利浦Hue智能灯泡套装是智能家居照明的理想选择，支持1600万种颜色和各种白光色温调节，可以通过手机APP远程控制，轻松设置个性化场景和定时任务。</p><p>套装包含Hue Bridge网关和彩色灯泡，支持与Alexa、Google Assistant、苹果HomeKit等主流智能家居系统无缝集成。支持智能情景联动，可以根据音乐、电视画面自动变换灯光颜色和亮度，打造沉浸式家庭氛围。</p>', '产品组成：Hue Bridge网关x1 + 彩色灯泡x3/x5\n灯泡功率：9W (相当于传统60W)\n灯泡接口：E27标准螺口\n亮度：800流明\n色温范围：2000K-6500K\n颜色：1600万种颜色\n寿命：25000小时\n无线连接：ZigBee, Wi-Fi\n兼容系统：iOS, Android, Alexa, Google Assistant, HomeKit\n控制方式：APP控制, 语音控制, 智能场景, 定时任务', 'Hue Bridge网关 x1\n彩色灯泡 x3或x5\n电源适配器 x1\n网线 x1\n快速入门指南 x1', '全国联保2年\n7天无理由退换\n90天换新\n终身技术支持\nAPP免费升级'),
(10, '<p>索尼Alpha 7 IV是一款专业级全画幅微单相机，搭载3300万像素Exmor R CMOS传感器和BIONZ XR处理器，提供卓越的影像质量和处理速度。支持10fps连拍和先进的759点相位检测自动对焦系统。</p><p>视频性能出色，支持4K 60p 10bit 4:2:2视频录制，内置多种专业视频功能。5轴机身防抖系统有效减少抖动，提高手持拍摄稳定性。创新的人眼/动物眼/鸟类眼追踪对焦技术，让抓拍更精准。</p>', '传感器：3300万像素全画幅Exmor R CMOS传感器\n处理器：BIONZ XR\n自动对焦：759点相位检测AF点，实时眼部追踪(人/动物/鸟类)\n连拍速度：最高10fps\n视频：4K 60p 10bit 4:2:2, S-Log3, S-Cinetone\n显示屏：3.0英寸104万像素可翻转触摸屏\n取景器：369万像素OLED电子取景器\n防抖：5轴机身防抖\n卡槽：双卡槽(CFexpress Type A/SD)\n接口：USB-C, HDMI, 麦克风, 耳机\n电池：NP-FZ100, 约580张(EVF)/710张(LCD)', '相机机身 x1\n电池 x1\n充电器 x1\n肩带 x1\n镜头(标准镜头套装) x1\n说明书 x1\n保修卡 x1', '全国联保1年\n主要部件2年保修\n7天无理由退换\n电池半年保修\n专业售后技术支持');

-- 添加更多商品图片表测试数据
INSERT INTO `pms_product_images` (`product_id`, `image_url`, `sort`, `is_main`) VALUES 
(6, '/uploads/products/wh1000xm5/main.png', 0, 1),
(6, '/uploads/products/wh1000xm5/side.png', 1, 0),
(6, '/uploads/products/wh1000xm5/fold.png', 2, 0),
(6, '/uploads/products/wh1000xm5/detail1.png', 3, 0),
(7, '/uploads/products/miband8pro/main.png', 0, 1),
(7, '/uploads/products/miband8pro/side.png', 1, 0),
(7, '/uploads/products/miband8pro/wear.png', 2, 0),
(7, '/uploads/products/miband8pro/screen.png', 3, 0),
(8, '/uploads/products/huawei-v75s/main.png', 0, 1),
(8, '/uploads/products/huawei-v75s/side.png', 1, 0),
(8, '/uploads/products/huawei-v75s/back.png', 2, 0),
(8, '/uploads/products/huawei-v75s/interface.png', 3, 0),
(9, '/uploads/products/philips-hue/main.png', 0, 1),
(9, '/uploads/products/philips-hue/bulbs.png', 1, 0),
(9, '/uploads/products/philips-hue/bridge.png', 2, 0),
(9, '/uploads/products/philips-hue/app.png', 3, 0),
(10, '/uploads/products/sony-a7iv/main.png', 0, 1),
(10, '/uploads/products/sony-a7iv/front.png', 1, 0),
(10, '/uploads/products/sony-a7iv/back.png', 2, 0),
(10, '/uploads/products/sony-a7iv/top.png', 3, 0),
(10, '/uploads/products/sony-a7iv/bottom.png', 4, 0);

-- 添加更多商品评论表测试数据
INSERT INTO `pms_product_comments` (`user_id`, `product_id`, `order_id`, `order_item_id`, `title`, `content`, `rating`, `images`, `is_anonymous`, `is_show`) VALUES 
(10, 6, 1000010, 10010, '降噪效果非常惊艳', '从老款XM4升级到XM5，降噪效果有显著提升，尤其是对人声的过滤更加彻底。舒适度也有提升，长时间佩戴也不会感到不适。音质方面保持了索尼一贯的水准，低音浑厚但不会过于轰头，中高音细节丰富。', 5, '/uploads/comments/10010_1.jpg', 0, 1),
(11, 6, 1000011, 10011, '颜值和音质的完美结合', '外观设计简约时尚，白色款特别好看。降噪效果在嘈杂的地铁和办公室环境下表现优异，音质也相当不错，尤其是用LDAC连接时，细节表现很到位。唯一不满意的是价格有点高。', 4, '/uploads/comments/10011_1.jpg,/uploads/comments/10011_2.jpg', 0, 1),
(12, 7, 1000012, 10012, '性价比超高的智能手环', '屏幕比上代大了不少，显示效果更清晰，操作也更方便。运动数据监测准确度提高了，心率监测与专业设备相差不大。续航确实能达到宣传的14天，日常使用一两周充一次电就够了。', 5, '/uploads/comments/10012_1.jpg', 0, 1),
(13, 7, 1000013, 10013, '小米手环又进步了', '从小米手环5一路用到8 Pro，每一代都有明显提升。这代最大的改进是屏幕和UI交互，显示更多信息，触控反应更灵敏。健康监测功能丰富，能满足日常所需。缺点是充电器又换了，之前的不能用了。', 4, NULL, 0, 1),
(14, 8, 1000014, 10014, '超大屏幕，画质震撼', '75英寸大屏幕在客厅观影效果极佳，OLED屏幕的黑色表现力和色彩还原都很出色。搭载鸿蒙系统的智慧屏操作流畅，多设备互联功能很实用，用手机控制电视特别方便。音质也很不错，看电影时有不错的环绕感。', 5, '/uploads/comments/10014_1.jpg,/uploads/comments/10014_2.jpg', 0, 1),
(15, 9, 1000015, 10015, '让家变得更智能', '安装简单，APP设置直观易用。灯光效果非常赞，颜色变化丰富，可以根据不同场景设置不同氛围。与Alexa结合使用，语音控制非常方便。唯一遗憾的是灯泡价格偏高，扩展成本较大。', 4, '/uploads/comments/10015_1.jpg', 0, 1),
(16, 10, 1000016, 10016, '专业级摄影神器', 'A7IV是我使用过的最好的微单，3300万像素提供了足够的细节和后期裁剪空间。自动对焦速度快且准确，眼部追踪功能在拍摄人像时特别有用。视频拍摄功能全面，4K画质锐利，各种Log格式满足专业需求。电池续航也有很大提升。', 5, '/uploads/comments/10016_1.jpg,/uploads/comments/10016_2.jpg,/uploads/comments/10016_3.jpg', 0, 1);

-- 添加更多商品评分表测试数据
INSERT INTO `pms_ratings` (`user_id`, `product_id`, `rating`, `rating_type`) VALUES 
(10, 6, 5, 0), 
(10, 6, 5, 1), 
(10, 6, 5, 2), 
(10, 6, 4, 3), 
(11, 6, 4, 0),
(11, 6, 5, 1),
(11, 6, 4, 2),
(11, 6, 4, 3),
(12, 7, 5, 0),
(12, 7, 5, 1),
(12, 7, 5, 2),
(12, 7, 5, 3),
(13, 7, 4, 0),
(13, 7, 4, 1),
(13, 7, 5, 2),
(13, 7, 3, 3),
(14, 8, 5, 0),
(14, 8, 4, 1),
(14, 8, 5, 2),
(14, 8, 4, 3),
(15, 9, 4, 0),
(15, 9, 5, 1),
(15, 9, 4, 2),
(15, 9, 3, 3),
(16, 10, 5, 0),
(16, 10, 5, 1),
(16, 10, 5, 2),
(16, 10, 4, 3);

-- 添加更多库存变更日志表测试数据
INSERT INTO `pms_stock_logs` (`product_id`, `sku_id`, `change_type`, `change_amount`, `before_stock`, `after_stock`, `order_id`, `order_item_id`, `operator`, `remark`) VALUES 
(6, 18, 1, -2, 102, 100, 1000010, 10010, 'system', '订单扣减库存'),
(6, 19, 1, -1, 101, 100, 1000011, 10011, 'system', '订单扣减库存'),
(6, 18, 3, 50, 50, 100, NULL, NULL, 'admin', '补充库存'),
(7, 21, 1, -2, 302, 300, 1000012, 10012, 'system', '订单扣减库存'),
(7, 22, 1, -3, 303, 300, 1000013, 10013, 'system', '订单扣减库存'),
(7, 21, 3, 100, 200, 300, NULL, NULL, 'admin', '补充库存'),
(8, 25, 1, -1, 51, 50, 1000014, 10014, 'system', '订单扣减库存'),
(9, 27, 1, -2, 102, 100, 1000015, 10015, 'system', '订单扣减库存'),
(9, 27, 3, 30, 70, 100, NULL, NULL, 'admin', '补充库存'),
(10, 30, 1, -1, 21, 20, 1000016, 10016, 'system', '订单扣减库存'); 