# TruckFarm 开发任务拆分

版本：v0.4
日期：2026-05-13
状态：按 planning-and-task-breakdown 校准后的任务草案

## 1. 阶段总览

| 阶段 | 目标 | 状态 |
| --- | --- | --- |
| P0 | 方向重定义与文档 | 已完成 |
| P1 | 新技术栈验证与脚手架 | 未开始 |
| P2 | 旧项目盘点与迁移映射 | 进行中 |
| P3 | 管理系统底座 | 未开始 |
| P4 | 模拟引擎一期 | 未开始 |
| P5 | 非固定经营动作 | 未开始 |
| P6 | AI 辅助模拟 | 未开始 |
| P7 | 可视化沙盘 | 未开始 |
| P8 | 经营复盘 | 未开始 |
| P9 | 测试、部署和简历包装 | 未开始 |

## 2. 阶段编号与开发留痕规则

- 大目标统一使用 `P0`、`P1`、`P2` 这种编号，例如 `P4 模拟引擎一期`。
- 小目标统一使用 `P4.1`、`P4.2`、`P4.3` 这种编号，表示同一阶段下的可独立交付节点。
- 每次实际开发、联调或重构，都要能对应到一个小目标，例如 `P1.5 开发 Flyway 初始化`、`P4.2 开发天气生成`。
- 每次完成一个小目标后，必须同步更新本文件中的状态、完成内容、验证结果和下一步。
- 本文件是阶段进度和开发留痕的唯一主记录；README 和其他 docs 只保留展示或设计信息。
- 若设计发生较大变化，优先更新 `requirements`、`architecture`、`api`、`database`、`ui`、`migration` 中对应文档，再回写本文件状态。

推荐留痕格式：

```text
P1.5 开发 Flyway 初始化
- 状态：已完成
- 已完成内容：...
- 验证方式：...
- 遗留问题：...
- 下一步：...
```

## 3. 任务拆分规范（按 planning-and-task-breakdown）

本文件同时承担阶段计划和开发留痕。所有新增任务必须遵守以下结构，避免出现“实现某模块”这种不可验收的大任务。

### 3.1 单个任务模板

```text
### Pn.x 任务名称

描述：一句话说明本任务交付什么能力。

依赖：Pn.y / 无。

预计范围：XS / S / M，原则上不超过 5 个文件；超过则继续拆分。

验收标准：
- [ ] 可测试条件 1。
- [ ] 可测试条件 2。

验证方式：
- [ ] 可执行命令、构建命令或手动检查路径。

可能涉及文件：
- `path/to/file`
```

### 3.2 任务边界

- P1 只做兼容性验证和脚手架，不写具体业务功能。
- P2 只做旧代码盘点、迁移映射和归档策略，不移动旧代码，除非单独确认。
- P3 先落基础能力和基础数据，再进入模拟引擎。
- P4-P5 必须先保证本地规则和 random seed 可复现，再接 AI。
- P6 AI 只能扩展修正和解释，不能成为采购、库存、销售、资金落库的前置依赖。

### 3.3 阶段检查点

- Checkpoint P1：后端、前端、PostgreSQL、Redis 均能本地启动，兼容性结论写入本文件。
- Checkpoint P2：旧用户、部门、作物、菜单、SQL 线索均有迁移映射和“不原样迁移”说明。
- Checkpoint P3：登录、动态菜单、基础业务数据和资金账户可用。
- Checkpoint P5：不接 AI 也能完成非固定经营动作，结果可追踪、可复现。
- Checkpoint P9：README 能指导陌生人启动和演示，简历描述与真实功能一致。

### 3.4 风险与缓解

| 风险 | 影响 | 缓解 |
| --- | --- | --- |
| Spring Boot 4 生态依赖不稳定 | P1 脚手架受阻 | 先验证最小组合，未适配能力延后，不阻塞核心闭环 |
| 任务横向切太大 | 单次实现难验证 | 每个任务限定验收标准和验证方式，超过 5 个文件继续拆 |
| 项目退化成普通后台 | 简历亮点不足 | P4-P5 强制交付时间推进、事件、随机种子和动作结果页 |
| AI 过早接入 | 核心流程不稳定 | P5 完成本地规则闭环后再进入 P6 |
| 旧代码归档误删 | 历史资产丢失 | 归档前记录来源、启动方式和目录映射，并单独确认 |

## 4. P0 方向重定义与文档

目标：把项目从普通农场经营管理平台调整为农场经营数字沙盘，并把规划统一回收至现有文档体系。

任务：

- [x] 创建 `AGENTS.md` 开发规范。
- [x] 创建 `docs/requirements.md` 需求说明。
- [x] 创建 `docs/architecture.md` 架构设计。
- [x] 创建 `docs/api.md` 接口设计。
- [x] 创建 `docs/database.md` 数据库设计。
- [x] 创建 `docs/ui.md` 页面与交互设计。
- [x] 创建 `docs/migration.md` 旧版迁移说明。
- [x] 创建 `docs/tasks.md` 开发任务拆分。
- [x] 更新 `README.md`，说明旧项目时间记录、技术栈和路线。
- [x] 将规则引擎、AI 修正、随机种子、资金流水和事件日志设计并入现有需求、架构、数据库、接口文档。
- [x] 保持文档收敛到现有主文档，不新增平行设计文档体系。

验收标准：README 和 docs 能说明“管理系统是底座，模拟引擎是核心亮点，经营决策是主线”。

完成记录：

- 已将沙盘方向统一回写到 `README.md`、`AGENTS.md`、`docs/requirements.md`、`docs/architecture.md`、`docs/api.md`、`docs/database.md`、`docs/ui.md`、`docs/migration.md`、`docs/tasks.md`。
- 已明确开发留痕只回写 `docs/tasks.md`，避免额外增加文档分支。

## 5. P1 新技术栈验证与脚手架

目标：搭建前后端分离项目骨架和本地运行环境。

### P1.1 JDK 25 与 Spring Boot 4 最小应用验证

描述：验证本机 JDK 25、Maven Toolchain 和 Spring Boot 4 最小应用能编译并启动。

依赖：无。

预计范围：S。

验收标准：
- [ ] `backend/` 中存在最小 Spring Boot 4 应用和健康检查接口。
- [ ] Maven 使用 JDK 25 编译，版本信息和启动日志可追溯。
- [ ] 验证结论记录到本任务“完成记录”。

验证方式：
- [ ] `cd backend; mvn -q test`（Maven 未加入 PATH 时使用本机 Maven 绝对路径）。
- [ ] `cd backend; mvn -q -DskipTests package`（Maven 未加入 PATH 时使用本机 Maven 绝对路径）。
- [ ] 本地访问健康检查接口返回成功。

可能涉及文件：
- `backend/pom.xml`
- `backend/src/main/java/com/truckfarm/TruckFarmApplication.java`
- `backend/src/main/resources/application.yml`
- `docs/tasks.md`

完成记录：待验证。

### P1.2 PostgreSQL、Flyway 与 Redis 兼容验证

描述：验证 PostgreSQL、Flyway migration 和 Redis Starter 在目标技术栈下可用。

依赖：P1.1。

预计范围：M。

验收标准：
- [ ] Docker Compose 能启动 PostgreSQL 和 Redis 7。
- [ ] Flyway 可以执行最小 PostgreSQL migration，并可重复启动不报错。
- [ ] 后端能连接 PostgreSQL 和 Redis。
- [ ] 验证结论记录到本任务“完成记录”。

验证方式：
- [ ] `docker compose up -d postgres redis`。
- [ ] `cd backend; mvn -q test`（Maven 未加入 PATH 时使用本机 Maven 绝对路径）。
- [ ] 检查 Flyway schema history 和 Redis 连接日志。

可能涉及文件：
- `docker-compose.yml`
- `.env.example`
- `backend/src/main/resources/application-dev.yml`
- `backend/src/main/resources/db/migration/V1__init_compatibility_check.sql`
- `docs/tasks.md`

完成记录：待验证。

### P1.3 MyBatis、Security、JWT 与 OpenAPI 兼容验证

描述：验证 MyBatis 3、Spring Security、JWT 和 OpenAPI 依赖是否能与 Spring Boot 4 组合运行；MyBatis Plus 只记录兼容结论，不通过前不引入业务。

依赖：P1.1、P1.2。

预计范围：M。

验收标准：
- [ ] MyBatis 3 Mapper 能完成一次最小查询测试。
- [ ] Spring Security 能保护默认接口，并放行健康检查。
- [ ] JWT 工具能生成和解析测试 Token。
- [ ] OpenAPI 若可用则记录访问路径；若不可用则记录原因并延后。
- [ ] MyBatis Plus 兼容性结论明确为“可引入 / 延后 / 放弃”。

验证方式：
- [ ] `cd backend; mvn -q test`（Maven 未加入 PATH 时使用本机 Maven 绝对路径）。
- [ ] 手动访问健康检查、受保护接口和 OpenAPI 路径。

可能涉及文件：
- `backend/pom.xml`
- `backend/src/test/java/com/truckfarm/**`
- `docs/tasks.md`

完成记录：待验证。

### P1.4 后端脚手架整理

描述：在兼容验证基础上整理正式后端目录、配置和通用入口。

依赖：P1.1、P1.2、P1.3。

预计范围：M。

验收标准：
- [ ] `backend/` Maven 项目使用 `com.truckfarm` 包结构。
- [ ] 存在 `application.yml`、`application-dev.yml`、`application-test.yml`。
- [ ] 后端具备统一时区、JSON 序列化和健康检查。
- [ ] 暂不写业务 CRUD。

验证方式：
- [ ] `cd backend; mvn -q test`（Maven 未加入 PATH 时使用本机 Maven 绝对路径）。
- [ ] `cd backend; mvn -q -DskipTests package`（Maven 未加入 PATH 时使用本机 Maven 绝对路径）。

可能涉及文件：
- `backend/pom.xml`
- `backend/src/main/java/com/truckfarm/**`
- `backend/src/main/resources/application*.yml`

完成记录：待验证。

### P1.5 前端脚手架

描述：创建 Vue 3 + TypeScript + Vite 前端项目，并准备基础目录和路由。

依赖：无，可与 P1.2-P1.3 并行。

预计范围：M。

验收标准：
- [ ] `frontend/` 可以本地启动和生产构建。
- [ ] 安装 Element Plus、Pinia、Vue Router、Axios、ECharts。
- [ ] 存在 `api/`、`views/`、`layouts/`、`stores/`、`types/`、`utils/` 基础目录。
- [ ] 有基础布局、登录占位页和首页沙盘占位路由。

验证方式：
- [ ] `cd frontend; npm run build`。
- [ ] 本地访问前端首页无控制台启动错误。

可能涉及文件：
- `frontend/package.json`
- `frontend/vite.config.ts`
- `frontend/src/**`

完成记录：待验证。

### P1.6 环境编排与启动说明

描述：把 PostgreSQL、Redis、后端和前端的本地启动方式收敛到 Docker Compose 与 README。

依赖：P1.4、P1.5。

预计范围：S。

验收标准：
- [ ] `docker-compose.yml` 包含 PostgreSQL、Redis、后端和前端所需配置或明确分阶段启动方式。
- [ ] `.env.example` 只包含示例值，不包含真实密钥。
- [ ] README 有新版本地启动说明。

验证方式：
- [ ] `docker compose config`。
- [ ] 按 README 步骤能启动基础环境。

可能涉及文件：
- `docker-compose.yml`
- `.env.example`
- `README.md`
- `docs/tasks.md`

完成记录：待验证。

Checkpoint P1：后端、前端、PostgreSQL 和 Redis 都可以本地启动，兼容性结论已写入 P1.1-P1.6 完成记录。

## 6. P2 旧项目盘点与迁移映射

目标：证明项目是基于旧项目迭代，而不是从零搭模板。

### P2.1 旧代码盘点

描述：盘点旧版 Controller、Service、DAO、Model、页面、菜单和 SQL 线索，确认可迁移资产与禁止原样迁移的问题。

依赖：无。

预计范围：S。

验收标准：
- [x] `docs/migration.md` 已记录旧代码、字段、页面和菜单盘点。
- [x] 已记录旧项目问题：明文密码、Session、SQL 拼接、脚本不完整、页面缺失。
- [x] 已记录验证方式和遗留问题。

验证方式：
- [x] 读取 `src/main/java/com/iweb` 目录结构。
- [x] 读取 `src/main/webapp/data/menu.json`、`table.sql` 与旧页面路径。

已完成内容：

- Controller：`LoginController`、`UserController`、`DepartmentController`、`PlantsController`、`MenuController`。
- Service：`UserServiceImpl`、`DepartmentServiceImpl`、`PlantsServiceImpl`、`MenuServiceImpl`、`CodeServiceImpl`。
- DAO：`UserDaoImpl`、`DepartmentDaoImpl`、`PlantsDaoImpl`、`MenuDaoImpl`、`CodeDaoImpl`。
- Model：`User`、`Department`、`Plants`、`Menu`、`Scheduled`、`Code`。
- DTO：`Result`、`PageDto`、`DataGrid`。
- 基础设施：`SessionFilter`、`DispatcherServlet`。
- 页面：`login.html`、`WEB-INF/view/main.html`、`user/index.html`、`user/add_user.html`、`user/change_pwd.html`、`department/index.html`、`plants/index.html`。
- 菜单：`src/main/webapp/data/menu.json` 使用静态菜单，存在“公司管理”“岗位管理”占位页 `/pages/1.html`，存在“采购管理”菜单但缺少真实页面 `/view/procurement/index`。
- SQL 线索：`table.sql` 仅提供部分 `tb_menu` 和 `tb_code` 脚本，缺少 `tb_user`、`tb_department`、`tb_plants`、`tb_scheduled` 等完整建表语句。

遗留问题：

- 还需要结合旧系统真实启动与实际库结构，进一步确认部分字段含义和缺失页面情况。

### P2.2 迁移映射补齐

描述：把旧版用户、部门、作物、菜单、编码池和疑似排班表映射到新版模块、表和“不原样迁移”说明。

依赖：P2.1。

预计范围：S。

验收标准：
- [x] 已建立迁移映射：用户、部门、作物、菜单、编码表。
- [x] 已明确哪些旧代码只作参考，哪些业务概念会迁移。
- [ ] 结合旧库真实启动和页面可达性，补充字段含义、缺失页面和可迁移数据样例。

验证方式：
- [ ] 读取 `docs/legacy-startup.md` 的旧版启动验证结果。
- [ ] 对照旧 DAO SQL 和新版 `docs/database.md` 表设计。

可能涉及文件：
- `docs/migration.md`
- `docs/database.md`
- `docs/tasks.md`

完成记录：进行中。

### P2.3 旧代码归档策略

描述：明确旧代码何时移动到 `legacy/`，以及移动前必须保留哪些来源说明和启动方式。

依赖：P1 Checkpoint。

预计范围：S。

验收标准：
- [ ] `backend/`、`frontend/` 脚手架稳定后再做归档决策。
- [ ] 归档前记录目录映射、Docker 启动方式和来源说明。
- [ ] 归档动作单独确认，不在普通文档修正中顺手移动旧代码。

验证方式：
- [ ] 归档前执行 `git status --short` 确认变更范围。
- [ ] 归档后旧版启动说明仍可指导定位历史代码。

可能涉及文件：
- `docs/migration.md`
- `docs/legacy-startup.md`
- `docs/tasks.md`

完成记录：未开始。

### P2.4 旧版永久留痕包

描述：旧版项目已整理为 `old/` 目录并压缩为 `old.zip`，当前仓库保留压缩包作为永久留痕；需要验证时再解压出 `old/` 执行。

依赖：P2.1。

预计范围：S。

验收标准：
- [x] `old.zip` 内的 `old/` 包含旧版 `src/`、`pom.xml`、`table.sql`、旧截图和旧版说明。
- [x] `old.zip` 内的 `old/` 包含 `Dockerfile`、`docker-compose.yml` 和 `legacy-init.sql`，解压后可独立用于旧版部署。
- [x] `old.zip` 内的 `old/README.md` 说明启动、停止、重置和归档用途。
- [x] `old.zip` 不包含 `target/`、`.class`、`.war` 等构建产物。

验证方式：
- [x] `old.zip` 清单确认包含旧版部署文件；解压时已执行 `cd old; docker compose config`。
- [x] `old.zip` 清单确认不包含构建产物；压缩前 `cd old; mvn -q -DskipTests package` 本地构建通过，构建产物验证后已删除。
- [ ] Docker daemon 当前未启动；待 Docker Desktop 启动后先解压 `old.zip`，再执行 `cd old; docker compose up -d --build` 做运行验证。

可能涉及文件：
- `old.zip`
- `docs/migration.md`
- `docs/tasks.md`

完成记录：已创建 `old/` 旧版留痕目录并压缩为 `old.zip`；当前仓库以 `old.zip` 作为永久留痕包保存。压缩包清单已确认包含旧源码、部署配置和说明文档，且不包含 `target/`、`.class`、`.war` 等构建产物；Docker 运行验证待 Docker daemon 可用后解压补充。

Checkpoint P2：能讲清旧项目有哪些可复用资产、为什么要重构、哪些问题不能原样迁移。

## 7. P3 管理系统底座

目标：先完成真实业务系统基础能力，为模拟引擎提供数据。

### P3.1 通用基础能力

- [ ] 实现 `Result<T>` 统一响应。
- [ ] 实现 `ErrorCode`、`BusinessException`、`GlobalExceptionHandler`。
- [ ] 实现分页请求和分页响应封装。
- [ ] 实现操作日志注解和切面。
- [ ] 实现当前用户上下文。
- [ ] 配置 OpenAPI 文档。

### P3.2 认证授权

- [ ] 实现用户登录接口。
- [ ] 实现 BCrypt 密码加密。
- [ ] 实现 JWT 生成和解析。
- [ ] 实现 Spring Security 过滤器。
- [ ] 实现刷新 Token。
- [ ] 实现退出登录和 Redis 黑名单。
- [ ] 实现当前用户信息和动态菜单接口。

### P3.3 业务基础数据

- [ ] 用户、角色、菜单、部门、员工。
- [ ] 作物分类、作物档案、作物生长规则。
- [ ] 地块档案、地块基础状态。
- [ ] 供应商、客户。
- [ ] 库存、库存流水。
- [ ] 采购单、销售单基础流程。

### P3.4 经营收支底座

- [ ] 经营账户初始化。
- [ ] 采购支出、销售收入、手工调整资金流水。
- [ ] 首页沙盘和复盘可汇总当前余额、收入、成本。

验收标准：系统可以登录，并维护模拟所需基础数据。

## 8. P4 模拟引擎一期

目标：实现“农场状态会随时间变化”。

### P4.1 模拟日历与天气

- [ ] 创建模拟日历表和当前日期接口。
- [ ] 实现手动推进一天。
- [ ] 实现天气生成或天气记录读取。

### P4.2 环境状态与生长状态

- [ ] 实现地块环境状态：湿度、肥力、病虫害风险。
- [ ] 实现作物生长状态：进度、健康度、预计产量。
- [ ] 实现作物生长规则的读取和计算。

### P4.3 事件与市场

- [ ] 实现市场价格波动。
- [ ] 实现 Tick 日志和事件日志。
- [ ] 编写模拟引擎单元测试。

验收标准：点击“推进一天”后，天气、地块、作物和事件会发生变化并可追踪。

## 9. P5 非固定经营动作

目标：用户同一类操作在不同环境下产生不同结果。

### P5.1 本地规则与随机种子

- [ ] 实现 `SimulationContextBuilder`。
- [ ] 实现 `RuleEvaluator` 基础接口。
- [ ] 实现 `OutcomeSampler` 和 random seed 保存。
- [ ] 实现 `SimulationOutcomeRecorder`。
- [ ] 支持 outcome replay。

### P5.2 操作结果模拟

- [ ] 采购结果：优品率、损耗率、延迟天数。
- [ ] 播种结果：出芽率、种子损耗、初始风险。
- [ ] 养护结果：浇水、施肥、除虫效果。
- [ ] 收获结果：产量、优品率、损耗率、入库质量。
- [ ] 销售结果：价格浮动、成交数量、客户满意度。
- [ ] 关键动作同步生成库存、资金和事件影响结果。

验收标准：不接 AI 也能通过本地规则 + 随机种子完成非固定结果模拟。

## 10. P6 AI 辅助模拟

目标：让 AI 根据环境上下文解释和修正模拟结果。

### P6.1 Prompt 与 Provider

- [ ] 设计 Prompt 模板和结构化 JSON 输出。
- [ ] 实现 `AiModifierService`。
- [ ] 实现 Mock AI Provider。

### P6.2 校验与降级

- [ ] 实现 `ModifierValidator`，限制修正因子范围。
- [ ] 实现 AI 失败 fallback。
- [ ] 编写 AI 输出解析和 fallback 测试。

### P6.3 AI 留痕

- [ ] 实现 AI 调用记录表和查询接口。
- [ ] 在结果详情中展示 AI 解释和 fallback 状态。

验收标准：AI 可以影响修正因子和解释，但不能直接修改库存、资金、订单状态等核心数据。

## 11. P7 可视化沙盘

目标：让项目截图明显区别于普通后台模板。

### P7.1 首页沙盘

- [ ] 首页沙盘：当前日期、天气、资金、风险、待办。
- [ ] 经营指标：库存预警、进行中计划、最近收益、市场行情。
- [ ] 经营叙事：今天发生了什么、接下来建议做什么。

### P7.2 地块与事件视图

- [ ] 地块沙盘：地块卡片或 2D 地块图。
- [ ] 事件时间线：天气、病虫害、市场波动、操作结果。
- [ ] 作物生长进度页面。

### P7.3 动作结果与趋势图

- [ ] 模拟动作结果页：基础规则、AI 修正、随机种子、最终结果。
- [ ] 市场价格、库存质量和资金趋势图表。

验收标准：别人看截图能一眼看出这是农场经营数字沙盘。

## 12. P8 经营复盘

目标：形成“决策 -> 结果 -> 复盘”的闭环。

### P8.1 经营结果汇总

- [ ] 生成本轮经营报告。
- [ ] 统计收入、成本、净利润。
- [ ] 统计作物产量、优品率、库存损耗。
- [ ] 统计资金流入、流出和现金余额变化。

### P8.2 决策影响分析

- [ ] 汇总关键风险事件。
- [ ] 说明用户决策对产量和收益的影响。
- [ ] AI 生成经营总结和下轮建议。

验收标准：一轮模拟结束后可以生成可展示的经营复盘报告。

## 13. P9 测试、部署和简历包装

目标：提高项目可信度和展示质量。

### P9.1 测试补齐

- [ ] 补充认证接口测试。
- [ ] 补充模拟引擎 Tick 测试。
- [ ] 补充 random seed 复现测试。
- [ ] 补充 AI fallback 测试。
- [ ] 补充收获入库、销售出库、库存不足、资金流水一致性测试。

### P9.2 部署和自动化

- [ ] 完善 Dockerfile 和 Docker Compose。
- [ ] 添加 GitHub Actions 后端构建、前端构建和测试。

### P9.3 简历展示包装

- [ ] 添加 README 截图、演示账号、启动步骤。
- [ ] 编写简历描述版本。
- [ ] 准备一条 3 到 5 分钟的稳定演示路径。

验收标准：README 可以指导陌生人启动项目；简历描述和实际功能一致；项目截图能体现业务和视觉质量。

## 14. 优先级规则

优先级从高到低：

1. 能跑起来。
2. 模拟主线完整。
3. 业务规则正确。
4. 非固定结果可追踪、可复现。
5. 页面能演示。
6. 测试和部署完善。
7. AI、附件、通知等增强能力。

## 15. 暂缓任务

- [ ] 复杂 3D 地图。
- [ ] 多人实时协作。
- [ ] 真实农业 IoT。
- [ ] 复杂财务总账。
- [ ] 多农场或多租户能力。
- [ ] 移动端专项适配。

## 16. 最近下一步

1. 以 `docs/tasks.md` 作为后续开发留痕主记录，确认 P1 与 P2 的推进顺序。
2. 开始 P1.1：验证 JDK 25 + Spring Boot 4 最小应用是否可稳定启动。
3. 开始 P1.2：验证 PostgreSQL、Flyway、Redis 与 Docker Compose 组合。
4. 推进 P2.2：依据旧版启动验证结果，补齐旧用户、部门、作物、菜单的字段映射和样例。
5. 完成 P1.4：整理 `backend/` Spring Boot 4 脚手架。
6. 完成 P1.5：创建 `frontend/` Vue 3 脚手架。
