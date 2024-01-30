create table t_user
(
    `id`              BIGINT(20) not null comment '用户ID，手机号码',
    `nickname`        VARCHAR(255) not null,
    `password`        VARCHAR(32)  default null comment 'MDS(MDS(pass明文+固定salt)+salt)',
    `slat`            VARCHAR(10)  default null,
    `head`            VARCHAR(128) default null comment '头像',
    `register_date`   datetime null comment '注册时间',
    `last_login_date` datetime     default null comment '最后一次登录时间',
    `login_count`     int(11) default '0' comment '登录次数',
    primary key (`id`)
)