# WhaleTide 鲸浪商城

## 项目简介

WhaleTide（鲸浪商城）是一个现代化的综合性电商平台，集成了移动端应用和管理后台系统。项目采用前后端分离架构，致力于为用户提供便捷的购物体验，为商家提供高效的管理工具。

### 核心功能

- **用户端功能**
  - 商品浏览与搜索
  - 购物车管理
  - 订单管理
  - 用户中心
  - 支付系统
  - 商品评价
  - 售后服务

- **商家端功能**
  - 商品管理
  - 订单处理
  - 库存管理
  - 数据统计
  - 营销活动
  - 客户管理
  - 财务管理

### 应用场景

- **B2C电商平台**：面向消费者的零售商城
- **O2O服务**：线上订购，线下服务
- **社交电商**：支持分享、拼团等社交化营销
- **多商户平台**：支持多商家入驻经营

## 项目技术栈

### 后端技术栈
- **核心框架**：Spring Boot 2.7.15
- **ORM 框架**：MyBatis-Plus 3.5.1
- **数据库**：MySQL 8.0
- **缓存**：Redis
- **安全框架**：Spring Security + JWT
- **接口文档**：Knife4j 4.4.0
- **工具库**：Hutool 5.8.9、Lombok、Druid 等

### 前端技术栈
#### 移动端 (whaletide-app-web)
- **框架**：uni-app
- **UI 库**：uView
- **状态管理**：Vuex
- **网络请求**：Axios
- **支付集成**：微信支付、支付宝支付

#### 管理后台 (admin-web)
- **框架**：Vue 2.7.2
- **UI 库**：Element UI 2.3.7
- **图表**：ECharts 4.2.0、v-charts 1.19.0
- **路由**：Vue Router 3.0.1
- **状态管理**：Vuex 3.0.1

## 项目结构

```
WhaleTide
├── admin-web/           # 管理后台前端
│   ├── src/             # 源代码
│   │   ├── views/       # 页面组件
│   │   ├── components/  # 公共组件
│   │   ├── router/      # 路由配置
│   │   ├── store/       # 状态管理
│   │   └── api/         # API 接口
│   ├── static/          # 静态资源
│   └── build/           # 构建配置
│
├── whaletide-app-web/   # 移动端应用前端 (uni-app)
│   ├── pages/           # 页面
│   │   ├── index/       # 首页
│   │   ├── category/    # 分类页
│   │   ├── cart/        # 购物车
│   │   ├── order/       # 订单
│   │   └── user/        # 用户中心
│   ├── components/      # 组件
│   ├── store/           # 状态管理
│   ├── api/             # API 接口
│   └── utils/           # 工具函数
│
├── src/                 # 后端源代码
│   └── main/
│       ├── java/
│       │   └── com/whale_tide/
│       │       ├── config/       # 配置类
│       │       ├── controller/   # 控制器
│       │       ├── service/      # 服务层
│       │       ├── mapper/       # 数据访问层
│       │       ├── entity/       # 实体类
│       │       ├── dto/          # 数据传输对象
│       │       ├── common/       # 公共类
│       │       └── util/         # 工具类
│       └── resources/            # 资源文件
│           ├── mapper/           # MyBatis映射文件
│           └── application.yml   # 应用配置
│
├── sql/                 # SQL 脚本
├── pom.xml              # Maven 配置
└── docker-compose.yml   # Docker 部署配置
```

## 安装与启动

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 5.0+
- Node.js 12+
- npm 6+

### 后端启动
1. 创建 MySQL 数据库，执行 `sql` 目录下的脚本
2. 修改 `src/main/resources/application.yml` 中的数据库连接信息
3. 启动 Redis 服务
4. 编译并启动项目：
   ```bash
   mvn clean package
   java -jar target/whale_tide-1.0.0-SNAPSHOT.jar
   ```

### 管理后台启动
1. 进入管理后台目录
   ```bash
   cd admin-web
   ```
2. 安装依赖
   ```bash
   npm install
   ```
3. 开发环境启动
   ```bash
   npm run dev
   ```
4. 生产环境构建
   ```bash
   npm run build
   ```

### 移动端启动
1. 进入移动端应用目录
   ```bash
   cd whaletide-app-web
   ```
2. 使用 HBuilderX 打开项目
3. 运行到浏览器或模拟器

### 使用 Docker 部署
项目支持使用 Docker Compose 快速部署，包含 Redis 和应用服务：
```bash
docker-compose up -d
```

## 项目特色

1. **高性能架构**
   - 采用分布式缓存提升系统性能
   - 数据库读写分离
   - 静态资源CDN加速

2. **安全可靠**
   - 完善的权限管理系统
   - 数据加密传输
   - 防SQL注入
   - XSS防护

3. **用户体验**
   - 响应式设计
   - 多端适配
   - 流畅的动画效果
   - 智能搜索推荐

4. **运营支持**
   - 丰富的营销工具
   - 数据统计分析
   - 会员管理系统
   - 多商户支持

## 待改进部分

1. **性能优化**
   - Redis 缓存优化
   - SQL 查询性能优化
   - 前端资源加载优化
   - 图片压缩与CDN优化

2. **功能完善**
   - 完善用户权限管理
   - 添加更多数据统计分析功能
   - 增强安全性措施
   - 增加更多营销工具

3. **架构升级**
   - 考虑微服务架构改造
   - 引入消息队列处理异步任务
   - 增加日志监控系统
   - 实现服务注册与发现

4. **测试完善**
   - 增加单元测试覆盖率
   - 添加接口自动化测试
   - 性能测试
   - 压力测试

5. **文档补充**
   - 完善 API 文档
   - 添加详细的部署文档
   - 编写开发规范
   - 添加用户使用手册

## 许可证

本项目使用 [MIT 许可证](LICENSE) 