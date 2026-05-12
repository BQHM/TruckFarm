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
('dept-001', _utf8mb4 0xE7A78DE6A48DE983A8, 1, 50000.00),
('dept-002', _utf8mb4 0xE99480E594AEE983A8, 0, 30000.00)
on duplicate key update name = values(name), count = values(count), fund = values(fund);

insert into tb_user(user_id, account, password, name, phone, create_time, dimission, role_name) values
('user-001', 'admin', '123456', _utf8mb4 0xE7AEA1E79086E59198, '13800000000', now(), '1', 'ADMIN')
on duplicate key update password = values(password), name = values(name), phone = values(phone), role_name = values(role_name);

insert into tb_scheduled(scheduled_id, user_id, department_id, remark) values
('sch-001', 'user-001', 'dept-001', '')
on duplicate key update user_id = values(user_id), department_id = values(department_id);

insert into tb_plants(plants_id, name, growth_cycle, price, class) values
('P001', _utf8mb4 0xE795AAE88C84, 90, 4.50, _utf8mb4 0xE894ACE88F9C),
('P002', _utf8mb4 0xE9BB84E7939C, 60, 3.20, _utf8mb4 0xE894ACE88F9C),
('P003', _utf8mb4 0xE88D89E88E93, 120, 12.80, _utf8mb4 0xE6B0B4E69E9C),
('P004', _utf8mb4 0xE78E89E7B1B3, 100, 2.60, _utf8mb4 0xE7B2AEE9A39F)
on duplicate key update name = values(name), growth_cycle = values(growth_cycle), price = values(price), class = values(class);

insert into tb_menu values
('dwzz', 'fa fa-coffee', _utf8mb4 0xE58D95E4BD8DE7BB84E7BB87, null, null, 'ADMIN,USER'),
('yhgl', 'fa fa-user', _utf8mb4 0xE794A8E688B7E7AEA1E79086, '/view/user/index', 'dwzz', 'ADMIN,USER'),
('lryh', 'fa fa-book', _utf8mb4 0xE5BD95E585A5E794A8E688B7, '/view/user/add_user', 'yhgl', 'ADMIN'),
('xtgl', 'fa fa-desktop', _utf8mb4 0xE7B3BBE7BB9FE7AEA1E79086, null, null, 'ADMIN,USER'),
('nzwgl', 'fa fa-leaf', _utf8mb4 0xE5869CE4BD9CE789A9E7AEA1E79086, '/view/plants/index', 'xtgl', 'ADMIN,USER')
on duplicate key update icon_cls = values(icon_cls), text = values(text), url = values(url), pid = values(pid), role_name = values(role_name);

insert into tb_code(code, state)
select lpad(seq.n, 4, '0'), '0'
from (
  select ones.n + tens.n * 10 + hundreds.n * 100 + thousands.n * 1000 + 1 as n
  from
    (select 0 n union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) ones
    cross join (select 0 n union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) tens
    cross join (select 0 n union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) hundreds
    cross join (select 0 n union all select 1 union all select 2 union all select 3 union all select 4 union all select 5 union all select 6 union all select 7 union all select 8 union all select 9) thousands
) seq
where seq.n <= 9999
on duplicate key update state = state;

