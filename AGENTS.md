# TruckFarm 开发规范

TruckFarm 是基于旧版 Servlet + MiniUI 毕设项目持续迭代升级的智慧农场经营管理平台。后续写代码、改文档、设计接口、拆任务和做简历化包装时必须遵守以下规则。

---

## 一、项目定位

TruckFarm 面向中小型农场、合作社或农产品经营场景，提供农场经营过程中的种植、库存、采购、销售、人员和数据看板管理能力。

项目定位必须保持清晰：

- 主场景：农场种植经营管理，不是通用后台模板。
- 核心目标：作物资料可维护、种植计划可追踪、采购库存可管理、销售收益可统计、人员权限可控制。
- 简历目标：体现基于旧项目边改边学、逐步替换技术栈、持续重构为现代前后端分离工程的能力。
- AI 定位：辅助分析、辅助录入、辅助生成建议，不能作为核心业务依赖。
- 不做方向：不做电商平台、不做农业物联网硬件平台、不做泛 ERP、不做纯后台管理脚手架。

任何新增功能都必须服务于农场经营闭环，不允许随意扩展成宽泛的“管理系统”。

---

## 二、旧版项目处理原则

当前仓库中存在旧版毕设代码，主要特征是：

- 后端：Servlet、Filter、手写 Controller / Service / Dao。
- 前端：MiniUI、jQuery、静态 HTML。
- 数据库：MySQL，SQL 脚本不完整。
- 架构：单体 war 包，前后端未分离。

重构时遵守以下原则：

1. 旧版代码是学习对照和迁移资产，不是废弃物；必须优先评估哪些模型、字段、SQL、页面流程、菜单和业务规则可以迁移到升级版架构。
2. 禁止在旧 Servlet 架构上继续堆功能；旧实现需要在学习和对照过程中被改造成 Spring Boot Controller / Service / Mapper、Vue 页面和升级版数据库脚本。
3. 旧版 `src/`、`pom.xml`、`table.sql` 在正式迁移前保持原样，不随意删除；需要归档时整体移动到 `legacy/` 并保留来源说明。
4. 迁移旧代码时必须记录映射关系，例如 `UserController -> modules/user`、`PlantsDaoImpl -> crop mapper`、`data/menu.json -> sys_menu seed`。
5. 可以复用旧版业务概念、字段设计、查询思路、页面操作流程和静态资源；不得原样复用明文密码、Session 鉴权、手写 DispatcherServlet、JDBC 连接管理、SQL 字符串拼接等问题实现。
6. 重构过程先补文档，再写代码；所有表结构、接口、页面和任务变更必须同步到 `docs/`。

---

## 三、目标项目结构

```text
truck-farm/
├── backend/                              # Spring Boot 后端应用
│   ├── src/main/java/com/truckfarm/
│   │   ├── TruckFarmApplication.java     # @SpringBootApplication + @EnableScheduling
│   │   │
│   │   ├── common/                       # 通用基础能力
│   │   │   ├── annotation/               # 操作日志、权限、限流等注解
│   │   │   ├── config/                   # Web、Redis、OpenAPI、Security、Jackson 配置
│   │   │   ├── constant/                 # 通用常量、Redis Key、业务字典
│   │   │   ├── exception/                # ErrorCode、BusinessException、GlobalExceptionHandler
│   │   │   ├── result/                   # Result<T> 统一响应结构
│   │   │   ├── security/                 # JWT、认证过滤器、用户上下文
│   │   │   ├── validation/               # 自定义参数校验
│   │   │   └── utils/                    # 通用工具类
│   │   │
│   │   ├── infrastructure/               # 技术基础设施
│   │   │   ├── ai/                       # AI Client、Prompt、Mock AI 服务
│   │   │   ├── file/                     # 本地文件 / MinIO 存储
│   │   │   ├── mapper/                   # MapStruct 转换器
│   │   │   ├── notification/             # 站内通知、邮件、Webhook 扩展
│   │   │   ├── redis/                    # RedisService、缓存 Key 管理
│   │   │   └── scheduler/                # 定时任务封装
│   │   │
│   │   └── modules/                      # 业务模块，每个模块自包含 MVC 分层
│   │       ├── auth/                     # 登录、刷新 Token、退出登录
│   │       ├── user/                     # 用户、角色、权限、菜单
│   │       ├── organization/             # 部门、岗位、员工资料
│   │       ├── crop/                     # 作物档案、作物分类、价格信息
│   │       ├── field/                    # 地块档案、面积、土壤、状态
│   │       ├── planting/                 # 种植计划、农事任务、收获记录
│   │       ├── supplier/                 # 供应商管理
│   │       ├── procurement/              # 采购单、采购明细、入库
│   │       ├── inventory/                # 库存、出入库流水、库存预警
│   │       ├── customer/                 # 客户档案
│   │       ├── sales/                    # 销售订单、出库、收入统计
│   │       ├── dashboard/                # 首页统计、趋势图、预警信息
│   │       ├── ai/                       # 经营分析、种植建议、数据总结
│   │       └── system/                   # 字典、配置、操作日志
│   │
│   └── src/main/resources/
│       ├── application.yml               # 应用配置
│       ├── application-dev.yml           # 开发环境配置
│       ├── application-test.yml          # 测试环境配置
│       ├── mapper/                       # MyBatis XML
│       └── prompts/                      # AI Prompt 模板
│
├── frontend/                             # Vue3 前端应用
│   ├── src/
│   │   ├── api/                          # API 请求封装
│   │   ├── assets/                       # 静态资源
│   │   ├── components/                   # 公共组件
│   │   ├── composables/                  # 组合式逻辑
│   │   ├── layouts/                      # 页面布局
│   │   ├── router/                       # Vue Router
│   │   ├── stores/                       # Pinia 状态管理
│   │   ├── styles/                       # 全局样式
│   │   ├── types/                        # TypeScript 类型
│   │   ├── utils/                        # 工具函数
│   │   └── views/                        # 页面组件
│   ├── package.json
│   └── vite.config.ts
│
├── docs/                                 # 项目文档
│   ├── requirements.md                   # 需求说明
│   ├── architecture.md                   # 架构设计
│   ├── api.md                            # 接口设计
│   ├── database.md                       # 数据库设计
│   ├── ui.md                             # 页面与交互设计
│   ├── migration.md                      # 旧版项目迁移说明
│   └── tasks.md                          # 开发任务拆分
│
├── docker-compose.yml                    # Docker 编排
├── .env.example                          # 环境变量示例
├── AGENTS.md                             # 本规范
└── README.md
```

**后端技术栈**：JDK 25 / Spring Boot 4 / Spring Security / JWT / MyBatis 3（优先）/ MyBatis Plus（通过兼容验证后引入）/ PostgreSQL / Redis 7 / Flyway / Spring Scheduler / MapStruct / SpringDoc OpenAPI（兼容验证后）/ MinIO 或本地文件存储 / Maven

**前端技术栈**：Vue 3 / TypeScript / Vite / Element Plus / Pinia / Vue Router / Axios / ECharts

**工程化能力**：Docker Compose / Flyway / JUnit 5 / Mockito / AssertJ / GitHub Actions

### Spring Boot 4 兼容性验证规则

Spring Boot 4 是本项目的前沿升级目标，P1 阶段必须先完成兼容性验证再大规模写业务代码：

- 验证本机 JDK 25、Maven Toolchain 和 Spring Boot 4 最小应用能稳定启动。
- 验证 PostgreSQL JDBC Driver、Flyway PostgreSQL migration 和 Redis Starter 可用。
- ORM 默认先使用 MyBatis 3 官方兼容方案；MyBatis Plus 只有在 Spring Boot 4 兼容验证通过后才能作为 CRUD 加速工具引入。
- OpenAPI 优先验证 SpringDoc；若 Spring Boot 4 生态暂未完全适配，先记录原因并延后接口文档增强。
- 兼容性验证结论必须记录到 `docs/tasks.md` 或新增 `docs/tech-validation.md`。
---

## 四、分层架构

```text
Controller -> Service -> Mapper
                ↕
        Infrastructure（RedisService、FileStorageService、AiClient、NotificationService）
```

### Controller 层

- 只负责路由、参数校验、权限入口和调用 Service，禁止写业务逻辑。
- RESTful 风格优先：`/api/{module}`、`/api/{module}/{id}`、`/api/{module}/{id}/{action}`。
- 请求体必须使用 `@Valid` + `@RequestBody` 校验。
- 分页查询统一使用 `PageQuery` 或模块专用 `XxxPageQuery`，不允许散落 `pageNum`、`pageSize` 参数。
- 返回值统一为 `Result<T>`，禁止直接返回裸对象、Entity 或 `Map`。

### Service 层

- 负责业务编排、状态流转、事务边界和跨模块调用。
- 大 Service 必须拆分，例如 `PlantingPlanService`、`InventoryFlowService`、`SalesOrderService`。
- 所有业务异常使用 `BusinessException(ErrorCode.XXX, message)`。
- AI 调用、文件上传、通知发送等外部操作必须与核心事务解耦。
- 涉及库存变动、订单状态、种植任务状态的操作必须保证事务一致性。

### Mapper 层

- 默认使用 MyBatis 3 Mapper 接口 + XML 管理数据访问。
- MyBatis Plus 仅在 Spring Boot 4 兼容验证通过后使用，简单 CRUD 可使用 `BaseMapper<XxxEntity>`。
- 复杂 SQL 必须写在 XML 中，禁止在 Service 中拼接 SQL。
- 列表查询必须考虑分页、索引、排序白名单和数据隔离条件。

### Infrastructure 层

- 封装技术细节，对业务层提供稳定接口。
- `RedisService` 统一管理 Redis 读写，不允许业务代码直接散落 `StringRedisTemplate` 操作。
- `FileStorageService` 屏蔽本地存储和 MinIO 差异。
- `AiClient` 屏蔽不同大模型 Provider 差异，并提供 Mock 实现。
- `NotificationService` 屏蔽站内通知、邮件、Webhook 等渠道差异。

---

## 五、JavaBean 后缀规则

| 后缀 | 用途 | 示例 |
| --- | --- | --- |
| `XxxEntity` | 数据库持久化对象 | `CropEntity`、`PlantingPlanEntity` |
| `XxxDTO` | 跨层数据传输 | `CropDetailDTO`、`DashboardSummaryDTO` |
| `XxxRequest` | 前端请求体 | `CreateCropRequest`、`CreateSalesOrderRequest` |
| `XxxResponse` | 前端响应体 | `CropListResponse`、`InventoryAlertResponse` |
| `XxxQuery` | 查询条件 | `CropPageQuery`、`SalesOrderPageQuery` |
| `XxxMapper` | MyBatis Mapper | `CropMapper`、`InventoryStockMapper` |
| `XxxConverter` | MapStruct 转换器 | `CropConverter`、`SalesOrderConverter` |
| `XxxProperties` | 配置属性 | `JwtProperties`、`FileStorageProperties` |

规则：

- 不可变请求对象优先使用 `record`，如 `CreateCropRequest`、`LoginRequest`。
- Entity 可以使用 Lombok，但必须避免暴露给前端。
- Entity 和 DTO / Response 的映射优先使用 MapStruct。
- 简单字段复制可以使用 `BeanUtils.copyProperties`，但复杂映射必须显式处理。
- **禁止直接返回 Entity 给前端**。

---

## 六、异常与错误码

### ErrorCode 分域规则

| 域 | 范围 | 示例 |
| --- | --- | --- |
| 通用 | 1xxx | `BAD_REQUEST(1001)`、`NOT_FOUND(1004)` |
| 认证与用户 | 2xxx | `USER_NOT_FOUND(2001)`、`PASSWORD_ERROR(2002)` |
| 组织权限 | 3xxx | `ROLE_NOT_FOUND(3001)`、`MENU_NOT_FOUND(3002)` |
| 作物档案 | 4xxx | `CROP_NOT_FOUND(4001)`、`CROP_STATUS_INVALID(4002)` |
| 地块管理 | 5xxx | `FIELD_NOT_FOUND(5001)`、`FIELD_STATUS_INVALID(5002)` |
| 种植计划 | 6xxx | `PLANTING_PLAN_NOT_FOUND(6001)`、`PLANTING_STATUS_INVALID(6002)` |
| 采购供应商 | 7xxx | `SUPPLIER_NOT_FOUND(7001)`、`PROCUREMENT_NOT_FOUND(7002)` |
| 库存管理 | 8xxx | `STOCK_NOT_ENOUGH(8001)`、`INVENTORY_FLOW_INVALID(8002)` |
| 销售客户 | 9xxx | `CUSTOMER_NOT_FOUND(9001)`、`SALES_ORDER_NOT_FOUND(9002)` |
| 文件存储 | 10xxx | `FILE_UPLOAD_FAILED(10001)`、`FILE_TYPE_NOT_ALLOWED(10002)` |
| AI 服务 | 11xxx | `AI_SERVICE_UNAVAILABLE(11001)`、`AI_PARSE_FAILED(11002)` |
| 系统配置 | 12xxx | `CONFIG_NOT_FOUND(12001)` |

### 异常处理规则

- 抛出：`throw new BusinessException(ErrorCode.XXX, "描述信息")`。
- **禁止** `throw new RuntimeException(...)`。
- 全局异常处理器 `GlobalExceptionHandler` 统一返回 `Result.error(code, message)`。
- `catch (BusinessException e) { throw e; }` 保留业务异常原样抛出。
- 禁止吞异常，禁止 `catch (Exception e) {}` 空处理。
- 第三方异常必须转换为业务异常或基础设施异常，并保留日志。

---

## 七、核心业务规则

### 用户、角色与权限

- 登录使用 Spring Security + JWT，密码必须使用 BCrypt 等安全算法加密。
- 菜单权限和按钮权限必须来自数据库，禁止使用静态 JSON 作为正式权限来源。
- 用户、角色、菜单、权限变更必须记录操作日志。
- 用户删除优先禁用或逻辑删除，避免破坏历史业务记录。

### 组织与员工

- 员工必须归属部门；部门可为空仅限系统初始化管理员。
- 员工离职不得物理删除，改为状态流转。
- 部门人数不允许长期依赖手动维护字段，优先通过统计或事务内一致更新。
- 与种植计划、采购单、销售单关联的员工数据必须保留历史引用。

### 作物档案

- 作物名称、分类、生长周期、参考价格和状态为核心字段。
- 作物删除优先逻辑删除，避免影响历史种植和销售记录。
- 作物参考价格用于统计和辅助录入，不能直接覆盖销售订单价格。
- 作物分类必须来自字典或分类表，禁止前端自由散落字符串。

### 地块管理

- 地块必须记录名称、面积、位置、状态等信息。
- 地块状态建议：`空闲`、`种植中`、`休耕`、`停用`。
- 同一地块同一时间段不允许存在冲突的种植计划。
- 地块面积使用 PostgreSQL `NUMERIC`，Java 侧使用 `BigDecimal`，禁止使用浮点类型。

### 种植计划

- 种植计划必须绑定作物、地块、负责人、计划开始日期和预计收获日期。
- 种植状态建议：`待开始 -> 种植中 -> 已收获`，可扩展 `已取消`。
- 预计收获日期不得早于计划开始日期。
- 收获记录必须保留产量、损耗、实际收获日期和备注。
- 取消计划不能删除已产生的农事任务和操作记录。

### 采购与库存

- 采购单必须绑定供应商，并保留采购明细。
- 采购入库后必须生成库存流水。
- 库存数量不得为负，出库前必须校验可用库存。
- 库存预警阈值可配置，低于阈值要在首页看板展示。
- 金额使用 PostgreSQL `NUMERIC`，Java 侧使用 `BigDecimal`，禁止使用 `Double` / `Float` 表示金额。

### 销售订单

- 销售订单必须绑定客户，并保留订单明细。
- 销售出库必须扣减库存并生成库存流水。
- 订单状态建议：`待确认 -> 待出库 -> 已完成`，可扩展 `已取消`。
- 已取消订单不参与销售收入统计。
- 销售收入、销量趋势和作物贡献度需要能在看板中体现。

### 数据看板

- 首页看板必须围绕经营闭环：种植面积、待办任务、库存预警、销售收入、采购成本。
- 统计口径必须在接口或文档中说明，避免前后端理解不一致。
- 复杂统计优先在 Service 中封装，前端只负责展示。
- 高频统计可以缓存，但数据库仍是最终事实来源。

---

## 八、AI 服务调用

AI 是辅助能力，不允许成为核心业务强依赖。

### 可用场景

- 根据作物、生长周期和地块信息生成种植建议。
- 根据采购、库存、销售数据生成经营摘要。
- 根据销售趋势生成补货或种植调整建议。
- 从票据文本中辅助提取供应商、金额、日期和明细。

### 调用规则

- 所有 AI 调用必须通过统一 `AiClient` 或 `AiService`，禁止在业务代码中直接调用第三方 SDK。
- 必须支持 `mock` Provider，开发测试时可以不配置真实 API Key。
- AI 失败不能影响作物创建、订单保存、库存流水生成等核心流程。
- AI 返回内容必须做长度限制、空值兜底和敏感词处理。
- Prompt 模板放在 `resources/prompts/`，禁止长 Prompt 散落在 Java 代码中。
- 敏感信息不得发送给 AI，例如用户密码、JWT、完整手机号、身份证号等。
- AI 结果只能作为建议展示，不能未经用户确认直接覆盖业务数据。

推荐接口：

```java
public interface AiClient {
  PlantingSuggestion suggestPlanting(PlantingContext context);

  OperationSummary summarizeOperation(OperationDataContext context);

  PurchaseReceiptParseResult parsePurchaseReceipt(String text);
}
```

---

## 九、Redis Key 规范

Redis Key 必须集中定义在常量类中，禁止在业务代码中随手拼接。

| 场景 | Key 示例 | 说明 |
| --- | --- | --- |
| 登录验证码 | `truckfarm:captcha:{uuid}` | 验证码缓存 |
| JWT 黑名单 | `truckfarm:auth:blacklist:{tokenId}` | 退出登录后 Token 失效 |
| 用户上下文 | `truckfarm:user:profile:{userId}` | 用户基础信息缓存 |
| 权限缓存 | `truckfarm:auth:permissions:{userId}` | 用户权限缓存 |
| 字典缓存 | `truckfarm:dict:{dictType}` | 字典数据缓存 |
| 库存预警 | `truckfarm:inventory:alert:{stockId}` | 库存预警去重 |
| 首页统计 | `truckfarm:dashboard:summary` | 首页看板缓存 |
| AI 任务状态 | `truckfarm:ai:task:{taskId}` | AI 异步任务状态 |

规则：

- Key 前缀统一使用 `truckfarm:`。
- TTL 必须显式设置，禁止无过期时间的临时缓存。
- 缓存更新必须考虑数据一致性，重要业务以数据库为准。
- 删除或更新业务数据时必须同步处理相关缓存。

---

## 十、文件与附件规范

- 支持的附件类型：采购票据、销售凭证、作物图片、地块图片、合同 PDF、质检单图片。
- 文件上传必须校验大小、扩展名和 MIME 类型。
- 文件访问必须鉴权，未登录用户不能访问业务附件。
- 文件元数据入库，文件内容存本地文件系统或 MinIO。
- 删除业务数据时不直接物理删除附件，优先逻辑删除或延迟清理。
- 文件路径、Bucket 名称和访问域名放配置文件，不允许硬编码。

---

## 十一、事务规则

- `@Transactional` 只放在 Service 层。
- 保持事务范围最小。
- **禁止**在事务中调用外部 API，如 AI、MinIO、邮件、Webhook。
- **禁止**同类内部调用 `@Transactional` 方法，避免 AOP 代理不生效。
- 批量插入和批量更新必须使用批处理能力，避免循环单条写库。
- 库存扣减、订单状态流转、种植计划状态流转必须加事务。
- 库存流水和业务单据状态必须在同一事务中保持一致。

---

## 十二、日志规范

- 使用 SLF4J，类上可使用 `@Slf4j`。
- 结构化日志：`log.info("Sales order created: orderId={}, customerId={}", orderId, customerId)`。
- 异常必须作为最后一个参数：`log.error("Inventory deduction failed: orderId={}", orderId, e)`。
- **禁止** `log.error("Error: {}", e.getMessage())`，会丢失堆栈。
- 日志中不得输出密码、Token、API Key、完整手机号等敏感信息。
- 定时任务必须打印开始、结束、处理数量和失败数量。

---

## 十三、数据库规范

- 数据库使用 PostgreSQL，数据库版本管理必须使用 Flyway。
- ORM 默认使用 MyBatis 3；MyBatis Plus 通过 Spring Boot 4 兼容验证后再引入。
- 表名使用小写下划线，业务表建议使用 `tf_` 前缀，系统表使用 `sys_` 前缀。
- 主键统一使用 `id`，类型优先 `BIGINT GENERATED BY DEFAULT AS IDENTITY`。
- 通用字段：`created_at`、`updated_at`、`created_by`、`updated_by`、`deleted`。
- 金额、数量和面积使用 PostgreSQL `NUMERIC`，Java 侧使用 `BigDecimal`，禁止使用浮点类型。
- 时间使用 PostgreSQL `TIMESTAMP`，Java 侧使用 `LocalDateTime`；纯日期使用 `DATE` / `LocalDate`。
- 逻辑删除使用 `deleted BOOLEAN DEFAULT FALSE` 字段，禁止随意物理删除核心业务数据。
- 常用查询条件必须加索引，如 `crop_id`、`field_id`、`status`、`order_no`、`created_at`。
- 字典值、状态值必须有枚举或字典表约束，禁止魔法字符串散落。

核心表建议：

```text
sys_user
sys_role
sys_user_role
sys_menu
sys_role_menu
sys_operation_log
sys_dict_type
sys_dict_data
tf_department
tf_employee
tf_crop_category
tf_crop
tf_field
tf_planting_plan
tf_farming_task
tf_harvest_record
tf_supplier
tf_procurement_order
tf_procurement_order_item
tf_customer
tf_sales_order
tf_sales_order_item
tf_inventory_stock
tf_inventory_flow
tf_file_resource
tf_ai_analysis
```

---

## 十四、配置管理

- 配置文件：`application.yml` + `application-{profile}.yml` + `.env`。
- 敏感信息放 `.env` 或环境变量，不入版本控制。
- `.env.example` 只放示例值，不放真实密钥。
- 业务配置使用 `@ConfigurationProperties`，如 `JwtProperties`、`FileStorageProperties`、`AiProperties`、`InventoryProperties`。
- **禁止** `@Value` 散落在 Service 中。
- 不同环境配置必须可切换：`dev`、`test`、`prod`。

---

## 十五、前端规范

- 前端使用 Vue 3 + TypeScript + Vite + Element Plus。
- 页面放在 `views/`，公共组件放在 `components/`。
- API 请求集中在 `api/`，禁止页面里直接散落 Axios URL。
- 类型定义集中在 `types/`，禁止大量使用 `any`。
- 状态管理使用 Pinia。
- 路由权限在 Router Guard 中处理。
- 表格页必须支持分页、筛选、排序和加载状态。
- 表单必须有前端校验，后端仍必须二次校验。
- ECharts 图表逻辑抽成组件或 composable，禁止塞满页面组件。
- 关键页面：登录页、首页看板、作物管理、地块管理、种植计划、采购管理、库存管理、销售订单、用户权限。

### 前端视觉方向

- 视觉风格应体现农场经营与数据管理结合，避免默认后台模板感过强。
- 色彩建议以自然绿色、土地色、中性灰为主，谨慎使用大面积高饱和色。
- 首页看板要有业务叙事：当前种什么、库存缺什么、销售赚多少、接下来做什么。
- 移动端至少保证登录、看板和核心列表可用。

---

## 十六、格式与命名

### Java

- 2 空格缩进，列限制 100 字符。
- 类名 UpperCamelCase，方法名 lowerCamelCase，常量 UPPER_SNAKE_CASE。
- 禁止通配符导入。
- 优先使用现代 Java 特性：`record`、`switch` 表达式、pattern matching、text blocks。
- 避免内联全限定类名，使用 import。
- 方法不要过长，超过 80 行优先拆分。
- 业务枚举必须有 code 和 description。

### TypeScript / Vue

- 2 空格缩进。
- 组件名 UpperCamelCase。
- 组合式 API 优先使用 `<script setup lang="ts">`。
- API 方法命名使用动词开头，如 `getCropPage`、`createPlantingPlan`、`updateSalesOrder`。
- 页面组件只做展示和交互编排，复杂逻辑抽到 composables 或 stores。

---

## 十七、测试规范

- 后端使用 JUnit 5 + Mockito + AssertJ。
- 测试方法使用 `@DisplayName` 中文描述测试意图。
- 复杂测试使用 `@Nested` 按功能分组。
- Service 层必须覆盖核心业务：种植计划状态流转、库存扣减、采购入库、销售出库、库存预警。
- Controller 层测试接口参数校验和权限拦截。
- 集成测试使用 `application-test.yml`。
- Redis 相关测试可使用真实 Redis 或 Testcontainers。
- AI 测试默认使用 Mock Provider，不依赖真实 API。
- 前端核心工具函数、权限路由和复杂表单逻辑需要补充测试。

---

## 十八、文档规范

- 需求变更先更新 `docs/requirements.md`。
- 架构调整更新 `docs/architecture.md`。
- 新增接口更新 `docs/api.md`。
- 新增或修改表结构更新 `docs/database.md`。
- 新增页面或交互变化更新 `docs/ui.md`。
- 旧版项目迁移策略更新 `docs/migration.md`。
- 开发任务拆分和进度更新 `docs/tasks.md`。
- README 面向项目展示，AGENTS 面向编码约束，不要混用。

---

## 十九、速查：禁止清单

| 禁止项 | 原因 |
| --- | --- |
| 在旧 Servlet 架构上继续堆新功能 | 迭代目标是用新技术替换旧架构，而不是继续扩大旧技术债 |
| `throw new RuntimeException(...)` | 绕过统一异常处理，必须用 `BusinessException` |
| 直接返回 Entity 给前端 | 暴露内部结构和敏感字段 |
| `@Value` 散落在 Service 中 | 配置应集中到 `@ConfigurationProperties` |
| 事务内调用 AI、文件存储、邮件、Webhook | 占用数据库连接，外部失败影响事务 |
| 同类内部调用 `@Transactional` 方法 | AOP 代理不生效 |
| `catch (Exception e) {}` 静默忽略 | 隐藏真实错误 |
| 循环单条查询或写库 | 性能差，改用批量操作 |
| 硬编码密钥、路径、Bucket、URL | 安全和可维护性风险 |
| 日志输出密码、Token、API Key | 安全风险 |
| Controller 写业务逻辑 | 破坏分层，难测试 |
| 页面直接拼接接口 URL | API 管理混乱 |
| AI 结果直接覆盖用户数据 | AI 只能辅助，用户必须确认 |
| 物理删除核心业务历史记录 | 破坏农场经营过程追溯 |
| 使用 `Double` / `Float` 表示金额 | 精度不可靠，必须使用 `BigDecimal` |
| SQL 字符串拼接排序字段 | 存在 SQL 注入风险，必须使用白名单 |

---

## 二十、当前阶段执行原则

项目当前处于旧项目评估和迭代升级方案准备阶段。本项目不是一次纯开发，而是边改、边学、边开发；后续执行时必须遵守：

1. 先完成项目文档和旧代码盘点，再开始脚手架和业务代码。
2. 先完成核心闭环：作物档案 -> 地块管理 -> 种植计划 -> 采购库存 -> 销售订单 -> 首页看板。
3. 认证权限、操作日志、接口文档和数据库版本管理必须作为基础能力优先落地。
4. Redis、文件存储、AI、通知等增强能力要解耦实现，不能阻塞核心功能。
5. AI 功能默认支持关闭和 Mock 模式。
6. 每个阶段完成后更新 README、任务进度和可演示截图。
7. 简历描述必须基于真实实现，并体现旧技术到新技术的替代过程，不夸大未完成能力。


