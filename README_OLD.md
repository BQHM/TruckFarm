# TruckFarm 旧版项目留档

这是 TruckFarm 毕业设计旧版项目的留档说明。旧版项目始于 2023 年 10 月，完成于 2024 年 1 月，采用传统 Java Web 技术栈实现了一个基础农场后台管理系统。

旧版代码当前仍保留在仓库根目录的 `src/`、`pom.xml`、`table.sql` 中，用作后续迭代升级的学习对照和迁移来源。

---

## 项目背景

旧版 TruckFarm 是毕业设计阶段完成的 Java Web 项目，主要目标是学习和实践：

- Servlet 请求分发
- Filter 登录拦截
- JDBC 数据库访问
- DAO / Service / Controller 分层
- MiniUI 后台页面开发
- WAR 包部署到 Tomcat

这个版本不是最终要继续扩展的架构，而是后续升级到 Spring Boot 3 + Vue 3 的基础版本。

---

## 旧版技术栈

| 类型 | 技术 |
| --- | --- |
| 后端 | Java Servlet、Filter、手写 Controller / Service / DAO |
| 前端 | HTML、jQuery、MiniUI |
| 数据库 | MySQL |
| 数据访问 | JDBC、Druid 连接池 |
| 构建 | Maven 普通 POM 项目 |
| 打包部署 | WAR 包 + Tomcat |
| 认证方式 | Session |
| JSON | FastJSON |
| 工具库 | Hutool、Lombok、pinyin4j |
| JDK | Java 8 编译目标 |
| Servlet API | `javax.servlet-api 3.1.0` |

说明：仓库没有 Maven Wrapper，因此 Maven 版本取决于本机环境。

---

## 已实现功能

| 模块 | 状态 | 说明 |
| --- | --- | --- |
| 登录 | 已实现 | 账号密码登录，登录后写入 Session |
| 登录拦截 | 已实现 | 使用 `SessionFilter` 拦截部分路径 |
| 用户管理 | 部分实现 | 用户列表、新增用户、修改手机号、修改密码 |
| 部门管理 | 部分实现 | 部门分页列表 |
| 农作物管理 | 部分实现 | 作物列表、分类筛选、名称搜索、修改价格、删除作物 |
| 菜单 | 部分实现 | 静态 `menu.json` 菜单，另有数据库菜单代码 |
| 公司管理 | 占位 | 当前使用 `pages/1.html` 临时占位页 |
| 岗位管理 | 占位 | 当前使用 `pages/1.html` 临时占位页 |
| 采购管理 | 未完成 | 菜单存在，但页面和后端未实现 |

---

## 目录说明

```text
TruckFarm/
├── src/main/java/com/iweb/
│   ├── controller/          # 旧版 Controller
│   ├── service/             # 旧版 Service
│   ├── dao/                 # JDBC DAO
│   ├── model/               # 实体模型
│   ├── dto/                 # 响应和分页 DTO
│   ├── filter/              # SessionFilter
│   ├── servlet/             # DispatcherServlet
│   └── utils/               # 数据源、JSON、字符串工具
│
├── src/main/resources/
│   └── db.properties        # 旧版数据库配置
│
├── src/main/webapp/
│   ├── login.html           # 登录页
│   ├── data/menu.json       # 静态菜单
│   ├── WEB-INF/view/        # 后台页面
│   ├── pages/1.html         # 临时占位页
│   └── scripts/miniui/      # MiniUI 资源
│
├── pom.xml                  # 旧版 Maven war 配置
├── table.sql                # 旧版不完整 SQL
├── legacy-init.sql          # 当前 Docker 启动用初始化 SQL
├── Dockerfile.legacy        # 旧版 Tomcat 镜像构建文件
└── docker-compose.legacy.yml
```

---

## Docker 启动方式

当前旧项目已经补充 Docker 启动配置。

### 1. 构建 WAR

```powershell
& 'D:\Software\Tools\Java tools\Maven\apache-maven-3.9.15\bin\mvn.cmd' -q -DskipTests package
```

如果本机已经配置 Maven PATH，也可以使用：

```powershell
mvn -q -DskipTests package
```

### 2. 启动旧服务

```powershell
docker compose -f docker-compose.legacy.yml up -d --build
```

### 3. 访问系统

```text
http://localhost:8090/login.html
```

演示账号：

```text
admin / 123456
```

### 4. 查看容器状态

```powershell
docker compose -f docker-compose.legacy.yml ps
```

### 5. 停止服务

```powershell
docker compose -f docker-compose.legacy.yml down
```

### 6. 清空数据库并重新初始化

```powershell
docker compose -f docker-compose.legacy.yml down -v
docker compose -f docker-compose.legacy.yml up -d --build
```

---

## 当前端口

由于本机可能已有其他服务占用默认端口，旧项目 Docker 启动时使用以下端口：

| 服务 | 容器端口 | 宿主机端口 |
| --- | --- | --- |
| Tomcat Web | 8080 | 8090 |
| MySQL | 3306 | 3307 |

---

## 已做的最小启动修复

为了让旧项目能构建并通过 Docker 启动，当前做过以下最小兼容修复：

- 补全 `CodeDaoImpl.selectAllCode()` 未完成代码。
- 修复 `CodeConstant` 静态方法访问实例字段导致的编译问题。
- 在 `pom.xml` 中补充 `pinyin4j` 依赖。
- 让 `DataSourceUtil` 支持通过环境变量覆盖数据库连接。
- 新增 `legacy-init.sql`，补齐旧项目启动需要的最小表结构和演示数据。
- 新增 `pages/1.html`，避免公司管理和岗位管理菜单 404。

这些修复只服务于旧项目启动和留档，不代表后续新架构会沿用相同实现。

---

## 已验证接口

当前 Docker 旧服务已验证：

```text
GET  /login.html   -> 200
POST /login        -> {"message":"login","success":true}
GET  /plants/list  -> 返回作物演示数据
GET  /user/list    -> 返回 admin 用户数据
```

---

## 已知问题

旧版项目存在以下问题，后续迭代升级时需要逐步修复：

- 数据库脚本 `table.sql` 不完整。
- 密码明文存储和明文比对。
- 使用 Session 认证，不适合前后端分离。
- 手写 `DispatcherServlet`，扩展性和可维护性弱。
- JDBC 样板代码多，事务和资源管理不完善。
- 多处 SQL 拼接排序字段，存在 SQL 注入风险。
- `SessionFilter` 没有覆盖所有业务路径。
- 首页菜单加载静态 `data/menu.json`，数据库菜单未真正接入首页。
- 用户管理前端展示可编辑多字段，但后端实际只更新手机号。
- 修改密码接口没有校验旧密码。
- 农作物删除是物理删除，不保留历史记录。
- 采购管理、公司管理、岗位管理等菜单未完整实现。
- 前端依赖 MiniUI 和 jQuery，缺少组件化和工程化能力。
- FastJSON 版本较旧，不适合作为后续最终技术方案。

---

## 后续升级方向

旧版项目后续会作为对照和迁移源，逐步升级为：

| 旧实现 | 升级方向 |
| --- | --- |
| Servlet / Filter | Spring Boot 3 / Spring MVC / Spring Security |
| Session | JWT + Redis Token 管理 |
| JDBC DAO | MyBatis Plus + XML |
| 手写 JSON 输出 | Spring MVC 自动序列化 + 统一响应 |
| MiniUI / jQuery | Vue 3 + TypeScript + Element Plus |
| `table.sql` 手工脚本 | Flyway / Liquibase 数据库版本管理 |
| WAR + 外置 Tomcat | Spring Boot 可执行 Jar / Docker 部署 |
| 静态菜单 JSON | 数据库动态菜单 + 前端动态路由 |

这个留档 README 用于记录旧项目的原始状态、启动方式、已知问题和后续迁移价值。
