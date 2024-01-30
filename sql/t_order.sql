create table `t_order`
(
    `id`               bigint(20) not null auto_increment comment '订单ID',
    `user_id`          bigint(20)     default null comment '用户ID',
    `goods_id`         bigint(20)     default null comment '商品ID',
    `delivery_addr_id` varchar(16)    default null comment '收货地址ID',
    `goods_name`       varchar(16)    default null comment '冗余过来的商品名称',
    `goods_count`      int(11)        default '0' comment '商品数量',
    `goods_price`      decimal(10, 2) default '0.00' comment '商品单价',
    `order_channel`    tinyint(4)     default '0' comment '1pc, 2android, 3ios',
    `status`           tinyint(4)     default '0' comment '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
    `create_time`      datetime       default null comment '订单创建时间',
    `pay_time`         datetime       default null comment '订单支付时间',
    primary key (`id`)
) engine = innodb
  auto_increment = 3
  default charset = utf8mb4;