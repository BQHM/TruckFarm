# TruckFarm 智慧农场经营管理平台

TruckFarm 是一个从毕业设计旧项目推倒重制的智慧农场经营管理平台。旧版项目始于 2023 年 10 月，完成于 2024 年 1 月，最初使用 Servlet、MiniUI、JDBC 和 MySQL 实现了登录、用户管理、部门管理、农作物管理等基础后台功能。

新版 TruckFarm 的目标不是简单修补旧代码，而是将其升级为一个适合作为简历项目展示的现代化前后端分离系统，围绕中小型农场经营场景，打通作物档案、地块管理、种植计划、采购库存、销售订单和数据看板的完整业务闭环。

> 当前项目处于重制规划和文档设计阶段，旧版代码暂时保留作为业务参考，新版代码将逐步迁移到 `backend/` 和 `frontend/`。

---

## 项目定位

TruckFarm 面向中小型农场、农业合作社或农产品经营团队，解决农场经营中常见的信息分散、库存不清、种植进度难追踪、销售数据难统计等问题。

核心目标：

- 作物资料可维护：分类、生长周期、参考价格、状态。
- 地块状态可追踪：空闲、种植中、休耕、停用。
- 种植计划可流转：待开始、种植中、已收获、已取消。
- 采购入库可追溯：供应商、采购单、采购明细、库存流水。
- 销售出库可校验：客户、销售订单、库存扣减、收入统计。
- 经营看板可展示：种植面积、库存预警、采购成本、销售趋势。
- 权限日志可审计：用户、角色、菜单、按钮权限、操作日志。

---

## 项目历史

| 时间 | 阶段 | 说明 |
| --- | --- | --- |
| 2023-10 | 旧版启动 | 作为毕业设计项目开始开发，采用传统 Java Web 技术栈 |
| 2024-01 | 旧版完成 | 完成登录、用户、部门、农作物等基础后台功能 |
| 2026-05 | 重制规划 | 重新定位为智慧农场经营管理平台，补充规范和设计文档 |
| 2026-05 之后 | 新版开发 | 按前后端分离架构重建后端、前端、数据库和部署体系 |

旧版技术栈：

- Java Servlet / Filter
- JDBC / Druid
- MySQL
- MiniUI / jQuery / HTML
- Maven war 包

新版将保留旧项目中的“农作物、用户、部门、菜单权限”等业务概念，但不会继续沿用旧 Servlet + MiniUI 架构。

---

## 技术栈规划

### 后端

| 技术 | 说明 |
| --- | --- |
| JDK 21 | Java 运行环境 |
| Spring Boot 3.x | 后端应用框架 |
| Spring Security | 认证与授权 |
| JWT | 无状态登录凭证 |
| MyBatis Plus | ORM 和基础 CRUD |
| MySQL 8 | 关系型数据库 |
| Redis 7 | Token 黑名单、验证码、权限缓存、看板缓存 |
| MapStruct | Entity / DTO / Response 转换 |
| SpringDoc OpenAPI 或 Knife4j | 接口文档 |
| Flyway 或 Liquibase | 数据库版本管理 |
| JUnit 5 / Mockito / AssertJ | 测试 |

### 前端

| 技术 | 说明 |
| --- | --- |
| Vue 3 | 前端框架 |
| TypeScript | 类型约束 |
| Vite | 构建工具 |
| Element Plus | 管理后台 UI 组件 |
| Pinia | 状态管理 |
| Vue Router | 路由管理 |
| Axios | HTTP 请求 |
| ECharts | 首页看板和统计图表 |

### 工程化

| 技术 | 说明 |
| --- | --- |
| Docker Compose | 本地和演示环境编排 |
| OpenAPI | 接口文档和调试 |
| GitHub Actions | CI 构建与测试 |
| ESLint / Prettier | 前端代码质量 |
| Maven | 后端构建 |

---

## 目标架构

```text
TruckFarm/
├── backend/              # Spring Boot 3 后端，后续新增
├── frontend/             # Vue 3 前端，后续新增
├── docs/                 # 项目设计文档
├── legacy/               # 旧版代码归档目录，后续视情况迁移
├── src/                  # 当前旧版代码，暂时保留
├── pom.xml               # 当前旧版 Maven 配置，暂时保留
├── table.sql             # 当前旧版 SQL，暂时保留
├── AGENTS.md             # 开发规范
└── README.md             # 项目展示说明
```

新版后端采用模块化单体结构：

```text
Controller -> Service -> Mapper
                ↕
        Infrastructure
```

业务模块规划：

- `auth`：登录、刷新 Token、退出登录。
- `user`：用户、角色、菜单、权限。
- `organization`：部门、员工资料。
- `crop`：作物档案、作物分类。
- `field`：地块档案、状态管理。
- `planting`：种植计划、农事任务、收获记录。
- `supplier`：供应商管理。
- `procurement`：采购单、采购明细、采购入库。
- `inventory`：库存、库存流水、库存预警。
- `customer`：客户档案。
- `sales`：销售订单、销售出库、收入统计。
- `dashboard`：首页统计和经营看板。
- `system`：字典、配置、操作日志。

---

## MVP 功能路线

第一版优先打通核心经营闭环：

```text
登录 -> 动态菜单 -> 作物档案 -> 地块管理 -> 种植计划 -> 采购入库 -> 库存管理 -> 销售出库 -> 首页看板
```

### P0 文档与方案

- [x] 开发规范：`AGENTS.md`
- [x] 需求说明：`docs/requirements.md`
- [x] 架构设计：`docs/architecture.md`
- [x] 接口设计：`docs/api.md`
- [x] 数据库设计：`docs/database.md`
- [x] 页面与交互设计：`docs/ui.md`
- [x] 旧版迁移说明：`docs/migration.md`
- [x] 任务拆分：`docs/tasks.md`
- [x] 对外展示 README：`README.md`

### P1 新版脚手架

- [ ] 创建 `backend/` Spring Boot 3 项目。
- [ ] 创建 `frontend/` Vue 3 + TypeScript 项目。
- [ ] 添加 MySQL、Redis、后端、前端的 Docker Compose。
- [ ] 添加 `.env.example` 和本地启动说明。

### P2 基础能力

- [ ] 统一响应、统一异常、错误码。
- [ ] Spring Security + JWT 登录认证。
- [ ] 用户、角色、菜单、按钮权限。
- [ ] 动态菜单和前端路由守卫。
- [ ] OpenAPI 接口文档。
- [ ] Flyway / Liquibase 初始化脚本。

### P3 农场业务闭环

- [ ] 作物分类和作物档案。
- [ ] 地块管理。
- [ ] 种植计划和收获记录。
- [ ] 供应商和采购入库。
- [ ] 库存和库存流水。
- [ ] 客户和销售出库。

### P4 看板和演示数据

- [ ] 首页经营指标。
- [ ] 销售趋势图。
- [ ] 作物销售排行。
- [ ] 库存预警和待收获事项。
- [ ] 初始化演示数据。

### P5 测试、部署和简历包装

- [ ] 核心 Service 单元测试。
- [ ] 关键接口集成测试。
- [ ] Docker 一键启动验证。
- [ ] GitHub Actions CI。
- [ ] 项目截图和演示说明。
- [ ] 简历描述和项目亮点整理。

---

## 文档索引

| 文档 | 说明 |
| --- | --- |
| `AGENTS.md` | 项目开发规范和编码约束 |
| `docs/requirements.md` | 需求说明、MVP 范围、验收标准 |
| `docs/architecture.md` | 架构设计、模块划分、认证授权、缓存设计 |
| `docs/api.md` | REST API 草案和统一响应约定 |
| `docs/database.md` | MySQL 表设计和核心数据模型 |
| `docs/ui.md` | 页面结构、视觉方向和核心交互 |
| `docs/migration.md` | 旧版项目现状、迁移策略和业务映射 |
| `docs/tasks.md` | 阶段任务拆分和开发路线 |

---

## 计划中的项目亮点

后续实现完成后，TruckFarm 将重点展示以下能力：

- 从传统 Servlet 项目重构到 Spring Boot 3 + Vue 3 前后端分离架构。
- 基于 Spring Security + JWT 实现登录认证、动态菜单和按钮权限。
- 使用 MyBatis Plus + Flyway 管理数据访问和数据库版本。
- 使用 Redis 处理 Token 黑名单、权限缓存、字典缓存和看板缓存。
- 通过事务保证采购入库、销售出库、库存流水的一致性。
- 使用 ECharts 展示销售趋势、作物排行、库存预警和经营指标。
- 使用 Docker Compose 提供一键启动的本地演示环境。
- 补充单元测试、接口测试和 CI，提升项目可信度。

---

## 当前状态

当前仓库仍保留旧版项目代码：

- `src/`：旧版 Java Web 源码和 MiniUI 页面。
- `pom.xml`：旧版 war 项目 Maven 配置。
- `table.sql`：旧版部分数据库脚本。

已完成新版重制文档：

- `AGENTS.md`
- `docs/requirements.md`
- `docs/architecture.md`
- `docs/api.md`
- `docs/database.md`
- `docs/ui.md`
- `docs/migration.md`
- `docs/tasks.md`

下一步将创建新版 `backend/` 和 `frontend/` 脚手架，并逐步归档旧版代码。

---

## 运行说明

当前阶段尚未提供新版可运行程序。旧版项目依赖本地 Maven、Servlet 容器和 MySQL，且数据库脚本不完整，不建议继续作为正式运行版本。

新版启动方式将在完成脚手架后补充，目标是：

```bash
# 启动 MySQL、Redis、后端、前端
docker compose up -d
```

---

## 许可证

本项目为个人学习和简历展示项目，后续如需开源将补充 License。
