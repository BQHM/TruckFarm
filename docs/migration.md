# TruckFarm 旧版迭代升级说明

版本：v0.1
日期：2026-05-12
状态：迁移草案

## 1. 迭代升级目标

旧版 TruckFarm 是毕业设计时期的传统 Java Web 项目。本次目标不是小修小补，也不是完全推倒重写，而是在旧项目基础上边重构、边检查旧代码不规范之处、边学习新技术替换旧技术。升级版会把旧项目中有价值的业务模型、字段、SQL、页面流程和菜单设计迁移到现代化、可维护、可演示的简历项目中。

迭代升级结果：

- 保留并迁移旧项目的农作物、用户、部门、菜单权限等业务概念和可用字段。
- 重构旧 Servlet + MiniUI 技术实现，将其迁移为 Java 25 + Spring Boot 4、MyBatis / MyBatis Plus（兼容验证后）、PostgreSQL + Flyway 和 Vue 3 结构。
- 新增地块、种植计划、采购库存、销售订单和数据看板，形成完整经营闭环。
- 建立标准的项目文档、代码规范和工程化流程。

## 2. 旧版项目现状

### 2.1 技术栈

| 层级 | 旧版技术 |
| --- | --- |
| 后端 | Servlet、Filter、手写 Controller / Service / Dao |
| 前端 | HTML、jQuery、MiniUI |
| 数据库 | MySQL、Druid |
| 构建 | Maven war |
| 权限 | Session + roleName 字符串过滤 |
| 部署 | 传统 Servlet 容器 |

### 2.2 已识别模块

| 旧模块 | 说明 |
| --- | --- |
| 登录 | 账号密码登录，Session 保存用户信息 |
| 用户管理 | 用户列表、新增用户、修改手机号、修改密码 |
| 部门管理 | 部门列表分页 |
| 农作物管理 | 作物列表、分类筛选、价格修改、删除 |
| 菜单 | 静态 JSON 菜单 + 数据库菜单代码 |
| 编码表 | `tb_code` 编码池，但未完整接入 |

### 2.3 主要问题

- 数据库脚本不完整，缺少 `tb_user`、`tb_department`、`tb_plants`、`tb_scheduled` 等建表语句。
- 部分菜单指向不存在页面，例如采购管理。
- 新增作物页面缺失。
- SQL 中存在排序字段拼接，存在注入风险。
- 密码明文存储和比对。
- 修改密码没有校验旧密码。
- 菜单正式加载使用静态 JSON，未完整使用数据库权限。
- `CodeConstant` 存在静态方法访问实例字段等设计问题。
- 缺少统一异常、统一响应、接口文档、测试和工程化部署。

## 3. 迭代迁移策略

### 3.1 代码策略

| 旧内容 | 处理方式 |
| --- | --- |
| `src/main/java` | 按模块迁移业务模型、Controller 流程、Service 规则和 DAO 查询思路 |
| `src/main/webapp` | 迁移页面信息架构、表格列、搜索条件、按钮操作和静态资源，UI 实现改为 Vue 3 |
| `pom.xml` | 提取旧依赖信息和 Java 版本记录；新版后端使用 `backend/pom.xml` |
| `table.sql` | 提取旧菜单、编码和旧表字段线索；新版使用 PostgreSQL + Flyway 重建脚本 |
| `db.properties` | 提取数据库连接信息作为本地参考；正式配置改为 `application.yml` + 环境变量 |

### 3.2 目录策略

短期保留旧结构：

```text
TruckFarm/
├── src/              # 旧版代码，暂时保留
├── pom.xml           # 旧版 Maven 配置，暂时保留
├── table.sql         # 旧版 SQL，暂时保留
├── AGENTS.md
└── docs/
```

新版开发开始后：

```text
TruckFarm/
├── legacy/           # 旧版代码整体归档，作为迁移源保留
├── backend/          # 新版后端
├── frontend/         # 新版前端
├── docs/
└── README.md
```

归档旧代码到 `legacy/` 前必须确认路径，避免误删用户代码。归档后旧代码仍作为学习对照和迁移源保留。

## 4. 业务映射

| 旧版概念 | 新版模块 | 迁移说明 |
| --- | --- | --- |
| `tb_user` 用户 | `sys_user` + `tf_employee` | 登录账号和员工资料拆分 |
| `role_name` 角色 | `sys_role` + `sys_user_role` | 角色规范化 |
| 静态菜单 JSON | `sys_menu` + `sys_role_menu` | 支持动态菜单和按钮权限 |
| `tb_department` 部门 | `tf_department` | 保留部门概念，补充树形结构和状态 |
| `tb_plants` 农作物 | `tf_crop` + `tf_crop_category` | 作物分类规范化 |
| `tb_scheduled` 排班/关联 | `tf_employee`、`tf_planting_plan` | 先确认旧字段含义，再迁移可用关联关系 |
| `tb_code` 编码池 | 统一编号生成器 | 改为雪花 ID + 业务单号生成规则 |

## 5. 数据迭代迁移方案

如果需要迁移旧数据，建议按以下步骤：

1. 备份旧库 `truck_farm`。
2. 补全旧表结构说明，确认字段含义。
3. 创建新版 PostgreSQL 数据库和 Flyway 初始化脚本。
4. 编写 MySQL -> PostgreSQL 的一次性迁移 SQL 或 Java migration 工具。
5. 迁移顺序：部门 -> 用户/员工 -> 角色 -> 菜单 -> 作物分类 -> 作物。
6. 迁移后进行数据校验。

用户迁移：账号迁移到 `sys_user.username`；密码不沿用明文，迁移时强制重置默认密码并 BCrypt 加密；姓名和手机号迁移到 `tf_employee`；角色字符串映射到 `sys_role`。

作物迁移：作物名称迁移到 `tf_crop.crop_name`；生长周期迁移到 `growth_cycle_days`；价格迁移到 `reference_price`；分类先去重写入 `tf_crop_category`，再关联作物。

## 6. 新版替代设计

| 旧版 | 新版 | 复用方式 |
| --- | --- | --- |
| `/login -> Session -> SessionFilter` | `/api/auth/login -> JWT -> Spring Security Filter -> Redis blacklist` | 复用登录流程概念和账号字段，不复用明文密码和 Session 鉴权 |
| `/data/menu.json` 静态菜单 | `sys_menu + sys_role_menu -> /api/auth/menus -> Vue Router 动态路由` | 复用菜单层级、标题、图标和 URL 作为初始化种子 |
| JDBC PreparedStatement + ResultSet | MyBatis Mapper + XML 复杂查询 + MapStruct；MyBatis Plus 兼容验证后用于基础 CRUD | 复用查询需求和字段映射，重写 SQL 安全和分页实现 |
| MiniUI datagrid + jQuery ajax | Vue 3 + Element Plus Table/Form + Axios API 模块 | 复用表格列、筛选条件、弹窗流程和操作按钮 |
| `db.properties` | `application.yml` + `.env` + 环境变量 + PostgreSQL 配置 | 仅作为本地开发连接参考，不提交真实敏感配置 |

## 7. 迁移阶段计划

### 阶段 1：文档与设计

- 完成 AGENTS 规范。
- 完成需求、架构、接口、数据库、UI、迁移、任务文档。
- 明确 MVP 范围。

### 阶段 2：新版脚手架

- 验证 Java 25 + Spring Boot 4 + PostgreSQL + Flyway + MyBatis / MyBatis Plus 兼容性。
- 创建 `backend/` Spring Boot 4 项目。
- 创建 `frontend/` Vue 3 项目。
- 添加 Docker Compose。
- 添加 README 和环境变量示例。

### 阶段 3：基础能力

- 认证授权。
- 用户角色菜单。
- 统一响应、异常、日志。
- OpenAPI 文档。
- Flyway 初始化脚本。

### 阶段 4：业务闭环

- 作物和地块。
- 种植计划和收获记录。
- 采购和库存。
- 销售和出库。
- 首页看板。

### 阶段 5：工程化与包装

- 单元测试和集成测试。
- CI。
- 演示数据。
- README 截图。
- 简历描述。

## 8. 风险与注意事项

| 风险 | 处理方式 |
| --- | --- |
| 旧代码和新代码混杂 | 新代码必须放 `backend/` 和 `frontend/`，旧代码归档到 `legacy/` 后只作为迁移源 |
| 数据库字段含义不清 | 不强行迁移旧数据，优先使用新版演示数据 |
| 功能范围膨胀 | 严格按 MVP 闭环推进 |
| 重构变成机械翻译旧代码 | 迁移业务价值和可用逻辑，同时修复旧架构、安全和工程化问题 |
| 删除旧文件造成不可恢复 | 移动或删除旧代码前必须确认并保留备份 |

## 9. 不原样迁移内容

以下内容不作为最终实现原样迁移，但可提取其中的业务意图：

- MiniUI 组件实现本身；表格列、表单字段和操作流程需要迁移到 Vue 页面。
- 旧版手写 DispatcherServlet；请求分发关系需要迁移为 Spring MVC Controller。
- 旧版 JDBC 连接管理和 DAO 实现；查询语义需要迁移为 MyBatis Mapper / XML，MyBatis Plus 只在兼容验证通过后用于基础 CRUD。
- 旧版 `tb_code` 静态编码池实现；编号需求可迁移为新版业务单号生成器。
- 不完整或无法确认含义的旧 SQL；确认字段含义后再迁移为新版脚本。

## 10. 迭代升级完成判定

- 升级版系统可以独立启动，不依赖旧 Servlet 运行架构。
- MVP 业务闭环可完整演示。
- 旧项目核心概念已在升级版中有合理映射。
- README 明确说明项目迭代升级背景、学习过程和技术亮点。
- 旧代码已归档到 `legacy/` 或明确标注为历史代码。



