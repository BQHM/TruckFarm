# TruckFarm 旧版项目启动检查

日期：2026-05-12
状态：已通过 Docker 启动验证

## 1. 本机环境检查

| 项目 | 检查结果 | 说明 |
| --- | --- | --- |
| JDK | 已安装 JDK 17 | 旧项目编译目标是 Java 8，JDK 17 可以编译，但最好使用 JDK 8 或 17 均可先启动验证 |
| Maven | 已安装但未加入 PATH | 可用路径：`D:\Software\Tools\Java tools\Maven\apache-maven-3.9.15\bin\mvn.cmd` |
| Tomcat | 已安装 Tomcat 9.0.24 | 路径：`D:\Software\Tools\Java tools\apache-tomcat-9.0.24` |
| MySQL 命令行 | PATH 中没有 mysql | DBeaver 驱动目录存在 mysql 客户端，但未发现本机 MySQL 服务 |
| Docker | 已安装 | 可用 Docker Compose 启动 MySQL |

## 2. 旧项目技术栈确认

从 `pom.xml` 确认：

- Maven 普通 POM 项目，无 Maven Wrapper。
- 打包方式：`war`。
- 编译版本：Java 8，`maven.compiler.source=1.8`、`maven.compiler.target=1.8`。
- Servlet 依赖：`javax.servlet-api 3.1.0`。
- 数据库驱动：`mysql-connector-java 8.0.33`。
- 连接池：Druid 1.2.18。
- JSON：fastjson 1.2.75。
- Lombok：1.18.28。

## 3. 已发现的启动阻塞点

### 3.1 Maven 不在 PATH

直接执行 `mvn` 会失败。

临时解决：

```powershell
& 'D:\Software\Tools\Java tools\Maven\apache-maven-3.9.15\bin\mvn.cmd' -DskipTests package
```

长期解决：把 Maven `bin` 目录加入系统 PATH，或给项目补 Maven Wrapper。

### 3.2 编译错误：CodeDaoImpl 未完成

旧代码 `src/main/java/com/iweb/dao/CodeDaoImpl.java` 中 `selectAllCode()` 方法未写完，导致 Maven 编译失败。

已临时修复：

- 补充 `ResultSet` 导入。
- 补全 `select * from tb_code` 查询和 `Code` 映射。

### 3.3 编译错误：缺少 pinyin4j 依赖

旧代码 `StrUtl` 使用 `net.sourceforge.pinyin4j.PinyinHelper`，但 `pom.xml` 原来没有 pinyin4j 依赖。

已临时修复：

```xml
<dependency>
    <groupId>com.belerweb</groupId>
    <artifactId>pinyin4j</artifactId>
    <version>2.5.1</version>
</dependency>
```

### 3.4 CodeConstant 静态方法访问实例字段

`CodeConstant.useCode()` 和 `freeCode()` 是静态方法，但访问了实例字段 `codeService`，会导致编译错误。

已临时修复：将 `codeService` 调整为 static。

> 这只是为了启动旧项目的兼容修复。后续新架构中不建议保留这种静态服务写法。

## 4. 当前构建状态

执行：

```powershell
& 'D:\Software\Tools\Java tools\Maven\apache-maven-3.9.15\bin\mvn.cmd' -q -DskipTests package
```

结果：构建成功，生成 war 包：

```text
target/book.war
```

## 5. 数据库启动要求

旧项目读取：

```text
src/main/resources/db.properties
```

当前配置：

```properties
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/truck_farm?characterEncoding=utf8&serverTimezone=GMT%2B8
jdbc.user=root
jdbc.pwd=123456
```

因此需要：

- 本地 MySQL 监听 `localhost:3306`。
- 数据库名：`truck_farm`。
- 用户名：`root`。
- 密码：`123456`。

本项目已补充 Docker Compose 旧服务启动配置：

```text
docker-compose.legacy.yml
Dockerfile.legacy
legacy-init.sql
```

由于本机 3306 和 8080 端口已被其他服务占用，旧服务端口映射为：

- MySQL：宿主机 `3307` -> 容器 `3306`
- Web：宿主机 `8090` -> 容器 `8080`

启动命令：

```powershell
& 'D:\Software\Tools\Java tools\Maven\apache-maven-3.9.15\bin\mvn.cmd' -q -DskipTests package
docker compose -f docker-compose.legacy.yml up -d --build
```

停止命令：

```powershell
docker compose -f docker-compose.legacy.yml down
```

如需清空旧数据库重新初始化：

```powershell
docker compose -f docker-compose.legacy.yml down -v
docker compose -f docker-compose.legacy.yml up -d --build
```

## 6. 数据库脚本问题

当前 `table.sql` 不完整，只包含：

- `tb_menu`
- `tb_code`

但旧代码实际还依赖：

- `tb_user`
- `tb_department`
- `tb_plants`
- `tb_scheduled`

如果不补表，应用能部署，但登录和业务接口会因为缺表报错。

## 7. 旧项目最小可运行建表草案

为了先跑起来，需要补充以下表和演示数据。字段顺序必须兼容旧 DAO 中的 `select *` 和 `insert into ... values(...)`。

```sql
create database if not exists truck_farm character set utf8mb4 collate utf8mb4_unicode_ci;
use truck_farm;

create table if not exists tb_menu (
  id varchar(10) primary key,
  icon_cls varchar(100),
  text varchar(100),
  url varchar(100),
  pid varchar(10),
  role_name varchar(20)
);

create table if not exists tb_department (
  department_id varchar(64) primary key,
  name varchar(100) not null,
  count int default 0,
  fund decimal(12,2) default 0
);

create table if not exists tb_user (
  user_id varchar(64) primary key,
  account varchar(64) not null unique,
  password varchar(64) not null,
  name varchar(100) not null,
  phone varchar(32),
  create_time datetime,
  dimission varchar(4),
  role_name varchar(20)
);

create table if not exists tb_scheduled (
  scheduled_id varchar(64) primary key,
  user_id varchar(64) not null,
  department_id varchar(64) not null,
  remark varchar(255)
);

create table if not exists tb_plants (
  plants_id varchar(64) primary key,
  name varchar(100) not null,
  growth_cycle int,
  price decimal(12,2),
  class varchar(100)
);

create table if not exists tb_code (
  code char(4) primary key,
  state char(1) default '0'
);

insert into tb_department(department_id, name, count, fund) values
('dept-001', '种植部', 1, 50000.00),
('dept-002', '销售部', 0, 30000.00)
on duplicate key update name = values(name);

insert into tb_user(user_id, account, password, name, phone, create_time, dimission, role_name) values
('user-001', 'admin', '123456', '管理员', '13800000000', now(), '1', 'ADMIN')
on duplicate key update password = values(password), role_name = values(role_name);

insert into tb_scheduled(scheduled_id, user_id, department_id, remark) values
('sch-001', 'user-001', 'dept-001', '')
on duplicate key update department_id = values(department_id);

insert into tb_plants(plants_id, name, growth_cycle, price, class) values
('P001', '番茄', 90, 4.50, '蔬菜'),
('P002', '黄瓜', 60, 3.20, '蔬菜'),
('P003', '草莓', 120, 12.80, '水果')
on duplicate key update price = values(price);

insert into tb_menu values
('dwzz', 'fa fa-coffee', '单位组织', null, null, 'ADMIN,USER'),
('yhgl', 'fa fa-user', '用户管理', '/view/user/index', 'dwzz', 'ADMIN,USER'),
('lryh', 'fa fa-book', '录入用户', '/view/user/add_user', 'yhgl', 'ADMIN'),
('xtgl', 'fa fa-desktop', '系统管理', null, null, 'ADMIN,USER'),
('nzwgl', 'fa fa-leaf', '农作物管理', '/view/plants/index', 'xtgl', 'ADMIN,USER')
on duplicate key update text = values(text), url = values(url), role_name = values(role_name);
```

## 8. Tomcat 启动要求

本机 Tomcat：

```text
D:\Software\Tools\Java tools\apache-tomcat-9.0.24
```

需要设置：

```powershell
$env:CATALINA_HOME='D:\Software\Tools\Java tools\apache-tomcat-9.0.24'
$env:JAVA_HOME='D:\Software\Tools\Java tools\jdk\jdk-17'
```

Tomcat 版本检查已通过：

```text
Apache Tomcat/9.0.24
```

## 9. 部署方式

构建成功后，将 war 部署到 Tomcat：

```powershell
Copy-Item -Force target\book.war 'D:\Software\Tools\Java tools\apache-tomcat-9.0.24\webapps\ROOT.war'
$env:CATALINA_HOME='D:\Software\Tools\Java tools\apache-tomcat-9.0.24'
$env:JAVA_HOME='D:\Software\Tools\Java tools\jdk\jdk-17'
& 'D:\Software\Tools\Java tools\apache-tomcat-9.0.24\bin\catalina.bat' run
```

访问：

```text
http://localhost:8090/login.html
```

演示账号：

```text
admin / 123456
```

## 10. 仍需注意的问题

- `SessionFilter` 没有拦截 `/department/*`，部门接口未受登录保护。
- 首页菜单当前加载的是 `/data/menu.json`，不是动态 `/menu/list`。
- `plants/index.html` 中新增作物指向 `/view/plants/add_plants`，但旧项目缺少该页面。
- 用户管理只更新手机号，前端显示可编辑项和后端实际更新能力不一致。
- 修改密码接口没有校验旧密码。
- SQL 排序字段仍然是字符串拼接，有 SQL 注入风险。
- `table.sql` 原脚本不完整，不能直接作为启动脚本。
- 旧版密码是明文，仅用于本地启动验证。

## 11. Docker 启动验证结果

已执行并通过以下验证：

```text
http://localhost:8090/login.html -> 200
POST http://localhost:8090/login admin/123456 -> {"message":"login","success":true}
GET /plants/list -> 返回 4 条作物演示数据
GET /user/list -> 返回 admin 用户数据
```

当前容器状态：

```text
truckfarm-legacy-mysql -> mysql:8.4 -> 3307:3306 -> healthy
truckfarm-legacy-web   -> tomcat war -> 8090:8080 -> running
```

说明：原计划使用 `mysql:8.0`，但本机已有 `mysql:8.4` 镜像且当前 Docker Hub 拉取 `mysql:8.0` 超时，因此 Compose 暂时使用 `mysql:8.4`。旧项目使用 `mysql-connector-java 8.0.33`，当前验证可以正常连接。
