# WhaleTide商城系统

WhaleTide是一个功能完善的电商系统，采用前后端分离架构，提供了商城客户端和管理后台两个界面。系统集成了创新功能，如智能购物助手和精美海报分享，以提升用户体验和社交传播能力。

## 项目架构

项目采用多模块Maven架构，包含以下核心模块：

- **whale-tide**：父模块，管理依赖版本和公共配置
- **whale-tide-common**：公共模块，包含工具类、常量、异常处理等
- **whale-tide-client**：客户端API模块，实现商城前台相关功能
- **whale-tide-admin**：管理端API模块，实现后台管理相关功能
- **wt-client-web**：客户端前端项目(Vue3)
- **wt-admin-web**：管理端前端项目(Vue2)

## 技术栈

### 后端技术栈

- **Spring Boot 2.7.9**：应用开发框架
- **MyBatis-Plus 3.5.3**：ORM框架
- **MySQL 8.0**：数据库
- **Redis**：缓存和分布式锁
- **Redisson**：分布式数据结构
- **RabbitMQ**：消息队列
- **JWT**：认证授权
- **Knife4j**：API文档
- **阿里云OSS**：对象存储
- **Hutool**：工具类库

### 前端技术栈

#### 客户端技术栈(wt-client-web)
- **Vue 3**：前端框架
- **Vue Router 4**：路由管理
- **Vuex 4**：状态管理
- **Element Plus**：UI组件库
- **Axios**：HTTP客户端

#### 管理端技术栈(wt-admin-web)
- **Vue 2**：前端框架
- **Vue Router 3**：路由管理
- **Vuex 3**：状态管理
- **Element UI**：UI组件库
- **ECharts**：数据可视化
- **Axios**：HTTP客户端

## 创新功能

### 1. 智能购物助手

基于商品评论和评分数据，提供智能购物建议，主要功能包括：
- 评论分析和关键词提取
- 商品推荐和优缺点分析
- 用户查询记录和个性化推荐

### 2. 精美海报分享

一键生成并分享商品精美海报，主要功能包括：
- 多种精美海报模板
- 支持自定义分享内容
- 商品二维码自动生成
- 分享数据统计和跟踪

## 数据库设计

项目包含多个数据库表，其中创新功能相关的主要表有：

- **comment_analysis**：评论分析表
- **shopping_assistant_record**：购物助手记录表
- **poster_template**：海报模板表
- **share_record**：分享记录表

## 快速开始

### 环境要求

- **JDK 1.8+**
- **Maven 3.6+**
- **MySQL 8.0+**
- **Redis 6.0+**
- **Node.js 14+**
- **npm 6+**

### 后端启动

1. 克隆代码库到本地
2. 导入数据库脚本(sql目录下)
3. 修改配置文件中的数据库和Redis连接信息
4. 在项目根目录执行：
```bash
mvn clean install
```
5. 依次启动以下模块：
```bash
cd whale-tide-admin
mvn spring-boot:run

cd ../whale-tide-client
mvn spring-boot:run
```

### 前端启动

#### 客户端前端

```bash
cd wt-client-web
npm install
npm run serve
```

#### 管理端前端

```bash
cd wt-admin-web
npm install
npm run serve
```

## 部署说明

### 后端打包

```bash
mvn clean package -Dmaven.test.skip=true
```

### 前端打包

```bash
# 客户端
cd wt-client-web
npm run build

# 管理端
cd wt-admin-web
npm run build
```

## 项目结构

```
whale-tide                                # 父模块
├── whale-tide-common                     # 公共模块
│   ├── src/main/java
│   │   └── com.whale_tide.common
│   │       ├── config                    # 配置类
│   │       ├── constant                  # 常量
│   │       ├── exception                 # 异常处理
│   │       ├── utils                     # 工具类
│   │       ├── nlp                       # NLP工具包
│   │       ├── search                    # 搜索工具包
│   │       └── share                     # 分享工具包
│
├── whale-tide-client                     # 客户端模块
│   ├── src/main/java
│   │   └── com.whale_tide.client
│   │       ├── controller                # 控制器
│   │       ├── service                   # 服务层
│   │       ├── mapper                    # MyBatis接口
│   │       ├── entity                    # 实体类
│   │       ├── assistant                 # 智能助手模块
│   │       ├── search                    # 搜索模块
│   │       └── poster                    # 海报模块
│
├── whale-tide-admin                      # 管理端模块
│   ├── src/main/java
│   │   └── com.whale_tide.admin
│   │       ├── controller                # 控制器
│   │       ├── service                   # 服务层
│   │       ├── mapper                    # MyBatis接口
│   │       ├── entity                    # 实体类
│   │       ├── assistant                 # 助手管理模块
│   │       └── search                    # 搜索管理模块
│
├── wt-client-web                         # 客户端前端(Vue3)
│   ├── public
│   ├── src
│   │   ├── assets                        # 静态资源
│   │   ├── components                    # 公共组件
│   │   ├── router                        # 路由配置
│   │   ├── store                         # 状态管理
│   │   ├── views                         # 页面
│   │   └── App.vue                       # 入口组件
│
├── wt-admin-web                          # 管理端前端(Vue2)
│   ├── public
│   ├── src
│   │   ├── assets                        # 静态资源
│   │   ├── components                    # 公共组件
│   │   ├── router                        # 路由配置
│   │   ├── store                         # 状态管理
│   │   ├── views                         # 页面
│   │   └── App.vue                       # 入口组件
│
└── sql                                   # SQL脚本目录
```

## 联系与支持

如需帮助或有任何疑问，请联系项目维护团队。

## 许可证

[MIT License](LICENSE) 