set names utf8mb4;
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
('dept-002', '销售部', 0, 30000.00),
('dept-003', '仓储部', 0, 20000.00)
on duplicate key update name = values(name), count = values(count), fund = values(fund);

insert into tb_user(user_id, account, password, name, phone, create_time, dimission, role_name) values
('user-001', 'admin', '123456', '管理员', '13800000000', now(), '1', 'ADMIN'),
('user-002', 'user', '123456', '普通用户', '13900000000', now(), '1', 'USER')
on duplicate key update password = values(password), name = values(name), role_name = values(role_name);

insert into tb_scheduled(scheduled_id, user_id, department_id, remark) values
('sch-001', 'user-001', 'dept-001', ''),
('sch-002', 'user-002', 'dept-002', '')
on duplicate key update department_id = values(department_id);

insert into tb_plants(plants_id, name, growth_cycle, price, class) values
('P001', '番茄', 90, 4.50, '蔬菜'),
('P002', '黄瓜', 60, 3.20, '蔬菜'),
('P003', '草莓', 120, 12.80, '水果'),
('P004', '玉米', 100, 2.60, '粮食')
on duplicate key update name = values(name), growth_cycle = values(growth_cycle), price = values(price), class = values(class);

insert into tb_menu values
('dwzz', 'fa fa-coffee', '单位组织', null, null, 'ADMIN,USER'),
('yhgl', 'fa fa-user', '用户管理', '/view/user/index', 'dwzz', 'ADMIN,USER'),
('lryh', 'fa fa-book', '录入用户', '/view/user/add_user', 'yhgl', 'ADMIN'),
('xtgl', 'fa fa-desktop', '系统管理', null, null, 'ADMIN,USER'),
('nzwgl', 'fa fa-leaf', '农作物管理', '/view/plants/index', 'xtgl', 'ADMIN,USER')
on duplicate key update icon_cls = values(icon_cls), text = values(text), url = values(url), pid = values(pid), role_name = values(role_name);

insert into tb_code(code, state)
select lpad(n, 4, '0'), '0'
from (
  select ones.i + tens.i * 10 + hundreds.i * 100 + thousands.i * 1000 + 1 as n
  from (select 0 i union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) ones
  cross join (select 0 i union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) tens
  cross join (select 0 i union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) hundreds
  cross join (select 0 i union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) thousands
) numbers
where n between 1 and 9999
on duplicate key update state = state;

