# TruckFarm 旧版迁移说明

版本：v0.1
日期：2026-05-12
状态：迁移草案

## 1. 迁移目标

旧版 TruckFarm 是毕业设计时期的传统 Java Web 项目。迁移目标不是小修小补，而是将其作为业务原型，重制为现代化、可维护、可演示的简历项目。

迁移结果：

- 保留旧项目的农作物、用户、部门等业务概念。
- 抛弃旧 Servlet + MiniUI 技术架构。
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

## 3. 迁移策略

### 3.1 代码策略

| 旧内容 | 处理方式 |
| --- | --- |
| `src/main/java` | 作为 legacy 参考，不继续扩展 |
| `src/main/webapp` | 作为旧 UI 参考，不迁移 MiniUI 代码 |
| `pom.xml` | 后续新版后端使用 `backend/pom.xml` |
| `table.sql` | 作为旧数据参考，新版使用 Flyway / Liquibase |
| `db.properties` | 不迁移，改为 `application.yml` + 环境变量 |

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
├── legacy/           # 旧版代码整体归档
├── backend/          # 新版后端
├── frontend/         # 新版前端
├── docs/
└── README.md
```

迁移旧代码到 `legacy/` 前必须确认路径，避免误删用户代码。

## 4. 业务映射

| 旧版概念 | 新版模块 | 迁移说明 |
| --- | --- | --- |
| `tb_user` 用户 | `sys_user` + `tf_employee` | 登录账号和员工资料拆分 |
| `role_name` 角色 | `sys_role` + `sys_user_role` | 角色规范化 |
| 静态菜单 JSON | `sys_menu` + `sys_role_menu` | 支持动态菜单和按钮权限 |
| `tb_department` 部门 | `tf_department` | 保留部门概念，补充树形结构和状态 |
| `tb_plants` 农作物 | `tf_crop` + `tf_crop_category` | 作物分类规范化 |
| `tb_scheduled` 排班/关联 | `tf_employee`、`tf_planting_plan` | 不直接迁移，按业务重建 |
| `tb_code` 编码池 | 统一编号生成器 | 改为雪花 ID + 业务单号生成规则 |

## 5. 数据迁移方案

如果需要迁移旧数据，建议按以下步骤：

1. 备份旧库 `truck_farm`。
2. 补全旧表结构说明，确认字段含义。
3. 创建新版数据库和 Flyway 初始化脚本。
4. 编写一次性迁移 SQL 或 Java migration 工具。
5. 迁移顺序：部门 -> 用户/员工 -> 角色 -> 菜单 -> 作物分类 -> 作物。
6. 迁移后进行数据校验。

用户迁移：账号迁移到 `sys_user.username`；密码不沿用明文，迁移时强制重置默认密码并 BCrypt 加密；姓名和手机号迁移到 `tf_employee`；角色字符串映射到 `sys_role`。

作物迁移：作物名称迁移到 `tf_crop.crop_name`；生长周期迁移到 `growth_cycle_days`；价格迁移到 `reference_price`；分类先去重写入 `tf_crop_category`，再关联作物。

## 6. 新版替代设计

| 旧版 | 新版 |
| --- | --- |
| `/login -> Session -> SessionFilter` | `/api/auth/login -> JWT -> Spring Security Filter -> Redis blacklist` |
| `/data/menu.json` 静态菜单 | `sys_menu + sys_role_menu -> /api/auth/menus -> Vue Router 动态路由` |
| JDBC PreparedStatement + ResultSet | MyBatis Plus BaseMapper + XML 复杂查询 + MapStruct |
| MiniUI datagrid + jQuery ajax | Vue 3 + Element Plus Table/Form + Axios API 模块 |
| `db.properties` | `application.yml` + `.env` + 环境变量 |

## 7. 迁移阶段计划

### 阶段 1：文档与设计

- 完成 AGENTS 规范。
- 完成需求、架构、接口、数据库、UI、迁移、任务文档。
- 明确 MVP 范围。

### 阶段 2：新版脚手架

- 创建 `backend/` Spring Boot 项目。
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
| 旧代码和新代码混杂 | 新代码必须放 `backend/` 和 `frontend/` |
| 数据库字段含义不清 | 不强行迁移旧数据，优先使用新版演示数据 |
| 功能范围膨胀 | 严格按 MVP 闭环推进 |
| 重构变成翻译旧代码 | 只迁移业务概念，不迁移旧实现 |
| 删除旧文件造成不可恢复 | 移动或删除旧代码前必须确认并保留备份 |

## 9. 暂不迁移内容

- MiniUI 组件和样式。
- 旧版手写 DispatcherServlet。
- 旧版 JDBC DAO 实现。
- 旧版 `tb_code` 编码池实现。
- 不完整或无法确认含义的旧 SQL。

## 10. 迁移完成判定

- 新版系统可以独立启动，不依赖旧 Servlet 项目。
- MVP 业务闭环可完整演示。
- 旧项目核心概念已在新版中有合理映射。
- README 明确说明项目重制背景和技术亮点。
- 旧代码已归档到 `legacy/` 或明确标注为历史代码。
