create table `t_goods`
(
    `id`           bigint(20) not null auto_increment comment '商品ID',
    `goods_name`   varchar(16)    default null comment '商品名称',
    `goods_title`  varchar(64)    default null comment '商品标题',
    `goods_img`    varchar(64)    default null comment '商品图片',
    `goods_detail` longtext comment '商品详情',
    `goods_price`  decimal(10, 2) default '0.00' comment '商品价格',
    `goods_stock`  int(11) default '0' comment '商品库存，-1表示没有限制',
    primary key (`id`)
) engine = innodb auto_increment=3 default charset=utf8mb4;