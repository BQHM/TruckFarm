# 建立权限表

create table tb_menu
(
    `id`        varchar(10) primary key,
    `icon_cls`  varchar(100),
    `text`      varchar(100),
    `url`       varchar(100),
    `pid`       varchar(10),
    `role_name` varchar(10)
);

select *
from tb_menu;

insert into tb_menu
values ('dwzz', 'fa fa-coffee', '单位组织', default, default, 'ADMIN,USER'),
       ('yhgl', 'fa fa-user', '用户管理', '/view/user/index', 'dwzz', 'ADMIN,USER'),
       ('lryh', 'fa fa-book', '录入用户', '/view/user/add_user', 'yhgl', 'ADMIN'),
       ('xtgl', 'fa fa-desktop', '系统管理', default, default, 'ADMIN,USER'),
       ('nzwgl', 'fa fa-leaf', '农作物管理', '/view/plants/index', 'xtgl', 'ADMIN,USER');

select *
from tb_menu
where pid is null;
select *
from tb_menu
where pid = 'yhgl';

select count(department_id)
from tb_department;

select *
from tb_department;

create table tb_code
(
    code  char(4) primary key,
    state char(1) default 0
);

DELIMITER //

CREATE PROCEDURE generate_codes()
BEGIN
    DECLARE x INT;
    DECLARE code VARCHAR(4);

    SET x = 1;

    WHILE x <= 9999 DO
            SET code = LPAD(CAST(x AS CHAR), 4, '0');
            INSERT INTO tb_code VALUES (code, DEFAULT);
            SET x = x + 1;
        END WHILE;

    COMMIT;
END //

DELIMITER ;
call generate_codes;
















