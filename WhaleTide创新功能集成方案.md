# WhaleTide创新功能集成方案

## 一、创新功能概述

本方案将为WhaleTide商城系统集成两个创新功能，提升用户体验和社交传播能力：

1. **智能购物助手**：基于商品评论和评分数据，提供智能购物建议
2. **精美海报分享**：一键生成并分享商品精美海报

这两个功能将在现有的模块化架构基础上实现，保持系统的高内聚低耦合特性。

## 二、技术方案

### 1. 智能购物助手实现方案

#### 核心技术选型
- **本地NLP处理**：使用轻量级NLP库（如HanLP、Ansj等）进行评论分析
- **规则引擎**：基于分析结果构建推荐规则
- **数据缓存**：Redis缓存分析结果，提高性能

#### 离线模拟大模型
- 预处理评论数据并生成分析结果
- 使用小型模型进行本地简单推理
- 实现模拟响应层，提供类似大模型的回复

### 2. 精美海报分享实现方案

#### 核心技术选型
- **前端渲染**：使用Canvas/html2canvas技术
- **海报模板**：预设多种精美模板
- **本地分享**：生成图片并本地保存
- **二维码集成**：生成包含商品信息的二维码

#### 本地演示方案
- 实现完整的分享流程界面
- 使用本地存储模拟分享统计
- 多浏览器窗口模拟社交分享效果

## 三、项目结构调整

基于《WhaleTide项目依赖结构优化方案》的多模块结构，进行如下调整：

```
whale-tide                                # 父模块
├── whale-tide-common                     # 公共模块
│   ├── src/main/java
│   │   └── com.whale_tide.common
│   │       ├── ...                       # 原有结构
│   │       ├── nlp                       # 新增：NLP工具包
│   │       │   ├── AnalysisConfig.java
│   │       │   ├── CommentAnalyzer.java
│   │       │   └── SentimentUtil.java
│   │       │
│   │       ├── search                    # 新增：搜索工具包
│   │       │   ├── ElasticsearchConfig.java
│   │       │   ├── SearchUtil.java
│   │       │   └── IndexUtil.java
│   │       │
│   │       └── share                     # 新增：分享工具包
│   │           ├── QrCodeUtil.java
│   │           └── ImageUtil.java
│
├── whale-tide-client                     # 客户端模块
│   ├── src/main/java
│   │   └── com.whale_tide.client
│   │       ├── ...                       # 原有结构
│   │       ├── assistant                 # 新增：智能助手模块
│   │       │   ├── controller
│   │       │   │   └── AssistantController.java
│   │       │   ├── service
│   │       │   │   ├── AssistantService.java
│   │       │   │   └── impl
│   │       │   │       └── AssistantServiceImpl.java
│   │       │   ├── dto
│   │       │   └── vo
│   │       │
│   │       ├── search                    # 新增：搜索模块
│   │       │   ├── controller
│   │       │   │   └── SearchController.java
│   │       │   ├── service
│   │       │   │   ├── SearchService.java
│   │       │   │   └── impl
│   │       │   │       └── SearchServiceImpl.java
│   │       │   ├── dto
│   │       │   └── vo
│   │       │
│   │       └── poster                    # 新增：海报模块
│   │           ├── controller
│   │           │   └── PosterController.java
│   │           ├── service
│   │           │   ├── PosterService.java
│   │           │   └── impl
│   │           │       └── PosterServiceImpl.java
│   │           ├── dto
│   │           └── vo
│
├── whale-tide-admin                      # 管理端模块
│   ├── src/main/java
│   │   └── com.whale_tide.admin
│   │       ├── ...                       # 原有结构
│   │       ├── assistant                 # 新增：助手管理模块
│   │       │   ├── controller
│   │       │   ├── service
│   │       │   └── vo
│   │       │
│   │       └── search                    # 新增：搜索管理模块
│   │           ├── controller
│   │           │   └── SearchManageController.java
│   │           ├── service
│   │           │   ├── IndexService.java
│   │           │   └── impl
│   │           │       └── IndexServiceImpl.java
│   │           └── vo
│
└── pom.xml                               # 父模块POM
```

## 四、数据库表设计

需要新增以下数据库表：

### 1. 评论分析表（comment_analysis）

```sql
CREATE TABLE `comment_analysis` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `keywords` varchar(500) DEFAULT NULL COMMENT '关键词提取',
  `positive_points` varchar(1000) DEFAULT NULL COMMENT '正面评价点',
  `negative_points` varchar(1000) DEFAULT NULL COMMENT '负面评价点',
  `recommend_score` decimal(3,1) DEFAULT NULL COMMENT '推荐指数',
  `analysis_time` datetime NOT NULL COMMENT '分析时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评论分析表';
```

### 2. 购物助手记录表（shopping_assistant_record）

```sql
CREATE TABLE `shopping_assistant_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `query` varchar(500) NOT NULL COMMENT '查询内容',
  `response` text NOT NULL COMMENT '助手响应',
  `product_ids` varchar(200) DEFAULT NULL COMMENT '关联商品ID列表',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物助手记录表';
```

### 3. 海报模板表（poster_template）

```sql
CREATE TABLE `poster_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_name` varchar(50) NOT NULL COMMENT '模板名称',
  `template_code` varchar(50) NOT NULL COMMENT '模板代码',
  `thumbnail` varchar(200) DEFAULT NULL COMMENT '缩略图URL',
  `background` varchar(200) DEFAULT NULL COMMENT '背景图URL',
  `config` text NOT NULL COMMENT '模板配置JSON',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_code` (`template_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='海报模板表';
```

### 4. 分享记录表（share_record）

```sql
CREATE TABLE `share_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `template_id` bigint(20) NOT NULL COMMENT '模板ID',
  `poster_path` varchar(200) DEFAULT NULL COMMENT '海报图片路径',
  `share_type` tinyint(1) NOT NULL COMMENT '分享类型：1-微信，2-朋友圈，3-QQ等',
  `view_count` int(11) DEFAULT '0' COMMENT '查看次数',
  `share_code` varchar(32) NOT NULL COMMENT '分享唯一码',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_product_id` (`product_id`),
  UNIQUE KEY `uk_share_code` (`share_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分享记录表';
```

## 五、依赖引入

为支持上述功能，需要在项目中引入以下依赖：

### 1. 父模块POM添加版本管理

```xml
<properties>
    <!-- 原有属性 -->
    <!-- ... -->
    
    <!-- 新增属性 -->
    <hanlp.version>1.8.3</hanlp.version>
    <ansj.version>5.1.6</ansj.version>
    <zxing.version>3.5.1</zxing.version>
    <thumbnailator.version>0.4.19</thumbnailator.version>
    <elasticsearch.version>7.17.10</elasticsearch.version>
    <spring-data-elasticsearch.version>4.4.12</spring-data-elasticsearch.version>
</properties>

<dependencyManagement>
    <dependencies>
        <!-- 原有依赖 -->
        <!-- ... -->
        
        <!-- NLP处理库 -->
        <dependency>
            <groupId>com.hankcs</groupId>
            <artifactId>hanlp</artifactId>
            <version>${hanlp.version}</version>
        </dependency>
        
        <!-- 二维码生成 -->
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>core</artifactId>
            <version>${zxing.version}</version>
        </dependency>
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>javase</artifactId>
            <version>${zxing.version}</version>
        </dependency>
        
        <!-- 图片处理 -->
        <dependency>
            <groupId>net.coobird</groupId>
            <artifactId>thumbnailator</artifactId>
            <version>${thumbnailator.version}</version>
        </dependency>
        
        <!-- Elasticsearch -->
        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-elasticsearch</artifactId>
            <version>${spring-data-elasticsearch.version}</version>
        </dependency>
        <dependency>
            <groupId>org.elasticsearch.client</groupId>
            <artifactId>elasticsearch-rest-high-level-client</artifactId>
            <version>${elasticsearch.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 2. 公共模块添加依赖

```xml
<dependencies>
    <!-- 原有依赖 -->
    <!-- ... -->
    
    <!-- NLP处理库 -->
    <dependency>
        <groupId>com.hankcs</groupId>
        <artifactId>hanlp</artifactId>
    </dependency>
    
    <!-- 二维码生成 -->
    <dependency>
        <groupId>com.google.zxing</groupId>
        <artifactId>core</artifactId>
    </dependency>
    <dependency>
        <groupId>com.google.zxing</groupId>
        <artifactId>javase</artifactId>
    </dependency>
    
    <!-- 图片处理 -->
    <dependency>
        <groupId>net.coobird</groupId>
        <artifactId>thumbnailator</artifactId>
    </dependency>
    
    <!-- Elasticsearch -->
    <dependency>
        <groupId>org.springframework.data</groupId>
        <artifactId>spring-data-elasticsearch</artifactId>
    </dependency>
    <dependency>
        <groupId>org.elasticsearch.client</groupId>
        <artifactId>elasticsearch-rest-high-level-client</artifactId>
    </dependency>
</dependencies>
```

### 3. 客户端模块无需额外依赖

客户端模块会继承公共模块的依赖，无需额外添加。

## 六、功能实现关键代码

### 1. NLP实现核心代码

#### 核心工具类（NlpProcessor.java）

```java
@Component
public class NlpProcessor {
    
    /**
     * 分词处理
     */
    public List<Term> segment(String text) {
        return HanLP.segment(text);
    }
    
    /**
     * 关键词提取
     */
    public List<String> extractKeywords(String text, int count) {
        return HanLP.extractKeyword(text, count);
    }
    
    /**
     * 简单情感分析
     * @return 0-1之间的分数，越大表示越正面
     */
    public double sentimentAnalysis(String text) {
        // 使用词典匹配的简单情感分析
        Set<String> positiveWords = loadPositiveWords();
        Set<String> negativeWords = loadNegativeWords();
        
        List<Term> terms = segment(text);
        int positiveCount = 0;
        int negativeCount = 0;
        
        for (Term term : terms) {
            if (positiveWords.contains(term.word)) {
                positiveCount++;
            } else if (negativeWords.contains(term.word)) {
                negativeCount++;
            }
        }
        
        if (positiveCount + negativeCount == 0) {
            return 0.5; // 中性评价
        }
        
        return (double) positiveCount / (positiveCount + negativeCount);
    }
    
    /**
     * 提取评论中的主要观点
     */
    public String extractMainPoint(String comment) {
        List<String> keywords = extractKeywords(comment, 3);
        if (keywords.isEmpty()) {
            return null;
        }
        
        // 组合前两个关键词形成主要观点
        if (keywords.size() >= 2) {
            return keywords.get(0) + keywords.get(1);
        }
        return keywords.get(0);
    }
}
```

#### 评论分析服务（CommentAnalysisService.java）

```java
@Service
public class CommentAnalysisService {
    
    @Autowired
    private NlpProcessor nlpProcessor;
    
    @Autowired
    private CommentAnalysisMapper analysisMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 批量分析商品评论
     */
    public void batchAnalyzeComments() {
        // 1. 获取所有有评论的商品ID
        List<Long> productIds = getProductIdsWithComments();
        
        // 2. 逐个分析商品评论
        for (Long productId : productIds) {
            analyzeComments(productId);
        }
    }
    
    /**
     * 分析单个商品的评论
     */
    private CommentAnalysis analyzeComments(Long productId) {
        // 1. 从Redis缓存获取
        String cacheKey = "comment:analysis:" + productId;
        CommentAnalysis analysis = (CommentAnalysis) redisTemplate.opsForValue().get(cacheKey);
        if (analysis != null) {
            return analysis;
        }
        
        // 2. 获取商品评论
        List<String> comments = getProductComments(productId);
        if (comments.isEmpty()) {
            return null;
        }
        
        // 3. 提取关键词
        Set<String> allKeywords = new HashSet<>();
        for (String comment : comments) {
            allKeywords.addAll(nlpProcessor.extractKeywords(comment, 5));
        }
        String keywords = String.join(",", allKeywords).substring(0, Math.min(500, String.join(",", allKeywords).length()));
        
        // 4. 分析积极评价点
        List<String> positivePoints = new ArrayList<>();
        for (String comment : comments) {
            if (nlpProcessor.sentimentAnalysis(comment) > 0.7) {
                String point = nlpProcessor.extractMainPoint(comment);
                if (point != null) {
                    positivePoints.add(point);
                }
            }
        }
        
        // 5. 分析消极评价点
        List<String> negativePoints = new ArrayList<>();
        for (String comment : comments) {
            if (nlpProcessor.sentimentAnalysis(comment) < 0.3) {
                String point = nlpProcessor.extractMainPoint(comment);
                if (point != null) {
                    negativePoints.add(point);
                }
            }
        }
        
        // 6. 计算推荐指数
        double positiveRatio = (double) positivePoints.size() / 
                             (positivePoints.size() + negativePoints.size() + 0.1);
        double recommendScore = Math.min(5.0, positiveRatio * 5.0);
        
        // 7. 保存分析结果
        analysis = new CommentAnalysis();
        analysis.setProductId(productId);
        analysis.setKeywords(keywords);
        analysis.setPositivePoints(String.join(",", positivePoints));
        analysis.setNegativePoints(String.join(",", negativePoints));
        analysis.setRecommendScore(recommendScore);
        analysis.setAnalysisTime(new Date());
        
        // 8. 存入数据库和缓存
        analysisMapper.insert(analysis);
        redisTemplate.opsForValue().set(cacheKey, analysis, 24, TimeUnit.HOURS);
        
        return analysis;
    }
}
```

#### 智能助手服务（AssistantServiceImpl.java）

```java
@Service
public class AssistantServiceImpl implements AssistantService {
    
    @Autowired
    private CommentAnalysisService commentAnalysisService;
    
    @Autowired
    private ProductService productService;
    
    // 预设回复模板
    private static final String[] RESPONSE_TEMPLATES = {
        "根据您的需求，我推荐{productName}，它的{feature}非常出色，很多用户反馈{positivePoint}。",
        "您好，考虑到您注重{feature}，{productName}会是不错的选择，它在这方面的评分是{rating}/5。",
        "分析您的需求后，我认为{productName}很适合您，因为它{positivePoint}，性价比也很高。"
    };
    
    /**
     * 根据用户查询生成智能购物建议
     */
    @Override
    public AssistantResponseVO getShoppingAdvice(AssistantQueryDTO queryDTO) {
        // 1. 分析用户查询，提取关键需求
        Set<String> userNeeds = extractUserNeeds(queryDTO.getQuery());
        
        // 2. 查找相关商品
        List<Product> relevantProducts = findRelevantProducts(userNeeds);
        
        // 3. 获取商品分析结果
        List<ProductAnalysisVO> productAnalyses = new ArrayList<>();
        for (Product product : relevantProducts) {
            CommentAnalysis analysis = commentAnalysisService.getAnalysis(product.getId());
            productAnalyses.add(convertToProductAnalysisVO(product, analysis));
        }
        
        // 4. 生成推荐回复
        String responseText = generateResponseText(userNeeds, productAnalyses);
        
        // 5. 保存查询记录
        saveQueryRecord(queryDTO, responseText, relevantProducts);
        
        // 6. 构建响应对象
        AssistantResponseVO response = new AssistantResponseVO();
        response.setResponseText(responseText);
        response.setProductAnalyses(productAnalyses);
        return response;
    }
    
    /**
     * 生成回复文本
     */
    private String generateResponseText(Set<String> userNeeds, List<ProductAnalysisVO> products) {
        if (products.isEmpty()) {
            return "抱歉，没有找到符合您需求的商品。请尝试其他关键词或浏览我们的推荐商品。";
        }
        
        // 选择排名第一的产品作为主推荐
        ProductAnalysisVO topProduct = products.get(0);
        
        // 随机选择一个模板
        Random random = new Random();
        String template = RESPONSE_TEMPLATES[random.nextInt(RESPONSE_TEMPLATES.length)];
        
        // 获取主要特性
        String mainFeature = getMainFeature(userNeeds, topProduct);
        
        // 获取正面评价点
        String positivePoint = getFirstPositivePoint(topProduct);
        
        // 填充模板
        return template.replace("{productName}", topProduct.getName())
                       .replace("{feature}", mainFeature)
                       .replace("{positivePoint}", positivePoint)
                       .replace("{rating}", String.format("%.1f", topProduct.getRecommendScore()));
    }
}
```

### 2. 海报生成功能实现

#### 二维码工具类（QrCodeUtil.java）

```java
@Component
public class QrCodeUtil {
    
    /**
     * 生成二维码
     * @param content 二维码内容
     * @param width 宽度
     * @param height 高度
     * @return 二维码图片的Base64字符串
     */
    public String generateQrCode(String content, int width, int height) {
        try {
            // 生成二维码
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);
            
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                content, BarcodeFormat.QR_CODE, width, height, hints);
            
            // 转换为图片
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            
            // 转Base64
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] bytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
            
        } catch (Exception e) {
            throw new RuntimeException("生成二维码失败", e);
        }
    }
}
```

#### 海报服务实现（PosterServiceImpl.java）

```java
@Service
public class PosterServiceImpl implements PosterService {
    
    @Autowired
    private PosterTemplateMapper templateMapper;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private QrCodeUtil qrCodeUtil;
    
    /**
     * 获取可用的海报模板
     */
    @Override
    public List<PosterTemplateVO> getAvailableTemplates() {
        List<PosterTemplate> templates = templateMapper.selectByStatus(1);
        return templates.stream()
                .map(this::convertToTemplateVO)
                .collect(Collectors.toList());
    }
    
    /**
     * 生成商品海报数据
     */
    @Override
    public PosterVO generatePoster(PosterGenerateDTO dto) {
        // 1. 获取商品信息
        Product product = productService.getProductById(dto.getProductId());
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        
        // 2. 获取模板信息
        PosterTemplate template = templateMapper.selectById(dto.getTemplateId());
        if (template == null || template.getStatus() != 1) {
            throw new BusinessException("海报模板不可用");
        }
        
        // 3. 生成分享码
        String shareCode = generateShareCode();
        
        // 4. 生成商品二维码
        String qrCodeBase64 = qrCodeUtil.generateQrCode(
            "https://example.com/product/" + product.getId() + "?share=" + shareCode, 
            200, 200);
        
        // 5. 保存分享记录
        saveShareRecord(dto.getUserId(), product.getId(), template.getId(), shareCode);
        
        // 6. 构建海报数据
        PosterVO poster = new PosterVO();
        poster.setShareCode(shareCode);
        poster.setProductInfo(buildProductInfo(product));
        poster.setTemplateInfo(buildTemplateInfo(template));
        poster.setQrCodeBase64(qrCodeBase64);
        
        return poster;
    }
    
    /**
     * 生成随机分享码
     */
    private String generateShareCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
```

#### 前端海报生成实现关键代码

```javascript
/**
 * 海报生成核心代码
 * @param {Object} posterData 海报数据
 * @param {string} containerId 容器ID
 */
function generatePoster(posterData, containerId) {
    // 1. 获取容器
    const container = document.getElementById(containerId);
    if (!container) return;
    
    // 2. 创建画布
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');
    canvas.width = 750;
    canvas.height = 1334;
    
    // 3. 绘制背景
    const backgroundImage = new Image();
    backgroundImage.crossOrigin = 'anonymous';
    backgroundImage.onload = function() {
        ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);
        
        // 4. 绘制商品图片
        drawProductImage(ctx, posterData.productInfo.imageUrl);
        
        // 5. 绘制商品信息
        drawProductInfo(ctx, posterData.productInfo);
        
        // 6. 绘制二维码
        drawQrCode(ctx, posterData.qrCodeBase64);
        
        // 7. 绘制分享提示
        drawShareTips(ctx);
        
        // 8. 转换为图片并显示
        const posterImage = document.createElement('img');
        posterImage.src = canvas.toDataURL('image/png');
        container.appendChild(posterImage);
        
        // 9. 添加保存按钮
        addSaveButton(container, canvas);
    };
    backgroundImage.src = posterData.templateInfo.background;
}

/**
 * 保存海报图片
 */
function savePoster(canvas) {
    // 创建下载链接
    const link = document.createElement('a');
    link.download = '商品海报.png';
    link.href = canvas.toDataURL('image/png').replace('image/png', 'image/octet-stream');
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}
```

## 七、接口定义

### 1. 智能购物助手接口

```java
@RestController
@RequestMapping("/api/assistant")
public class AssistantController {

    @Autowired
    private AssistantService assistantService;
    
    /**
     * 获取购物建议
     */
    @PostMapping("/advice")
    public ResponseEntity<AssistantResponseVO> getShoppingAdvice(@RequestBody AssistantQueryDTO queryDTO) {
        AssistantResponseVO response = assistantService.getShoppingAdvice(queryDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取商品分析
     */
    @GetMapping("/product-analysis/{productId}")
    public ResponseEntity<ProductAnalysisVO> getProductAnalysis(@PathVariable Long productId) {
        ProductAnalysisVO analysis = assistantService.getProductAnalysis(productId);
        return ResponseEntity.ok(analysis);
    }
}
```

### 2. 海报生成接口

```java
@RestController
@RequestMapping("/api/poster")
public class PosterController {

    @Autowired
    private PosterService posterService;
    
    /**
     * 获取海报模板列表
     */
    @GetMapping("/templates")
    public ResponseEntity<List<PosterTemplateVO>> getTemplates() {
        List<PosterTemplateVO> templates = posterService.getAvailableTemplates();
        return ResponseEntity.ok(templates);
    }
    
    /**
     * 生成商品海报
     */
    @PostMapping("/generate")
    public ResponseEntity<PosterVO> generatePoster(@RequestBody PosterGenerateDTO generateDTO) {
        PosterVO poster = posterService.generatePoster(generateDTO);
        return ResponseEntity.ok(poster);
    }
    
    /**
     * 根据分享码查看海报
     */
    @GetMapping("/view/{shareCode}")
    public ResponseEntity<PosterViewVO> viewPoster(@PathVariable String shareCode) {
        PosterViewVO view = posterService.viewByShareCode(shareCode);
        return ResponseEntity.ok(view);
    }
}
```

## 八、开发与集成建议

### 1. NLP功能开发步骤

1. **准备工作**：
   - 添加HanLP依赖
   - 下载HanLP所需的模型文件
   - 创建词库（正面、负面词汇）

2. **开发顺序**：
   - 先实现基础分词和关键词提取
   - 然后开发情感分析功能
   - 最后实现完整的评论分析和推荐逻辑

3. **数据准备**：
   - 预先准备50-100条高质量评论样本
   - 设计10-15个常见问题及回答模板

4. **测试验证**：
   - 单元测试各NLP功能
   - 验证评论分析结果准确性
   - 测试助手回复的合理性

### 2. 海报生成功能开发步骤

1. **设计海报模板**：
   - 设计3-5种不同风格模板
   - 定义各元素位置和样式
   - 准备高质量背景图

2. **开发顺序**：
   - 先实现二维码生成
   - 然后开发海报数据组装
   - 最后实现前端Canvas渲染

3. **前端优化**：
   - 添加海报预览功能
   - 实现简单的模板定制
   - 优化图片保存体验

4. **测试验证**：
   - 测试不同设备上的渲染效果
   - 验证图片保存功能
   - 测试二维码扫描可用性

## 九、演示策略

1. **多级演示方案**
   - 准备基础演示版本（完全离线可用）
   - 开发增强版演示（可选连接模拟API）
   - 制作概念视频展示完整功能愿景

2. **数据准备**
   - 精选10-15个样例商品数据
   - 为每个商品准备30-50条高质量评论
   - 预生成分析结果，确保演示流畅

3. **场景化演示**
   - 设计3-5个典型用户场景
   - 准备详细演示脚本
   - 模拟用户决策过程 