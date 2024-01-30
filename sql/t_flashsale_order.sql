create table `t_flashsale_order`
(
    `id`       bigint(20) not null auto_increment comment '秒杀订单ID',
    `user_id`  bigint(20) default null comment '用户ID',
    `order_id` bigint(20) default null comment '订单ID',
    `goods_id` bigint(20) default null comment '商品ID',
    primary key (`id`)
) engine=innodb auto_increment=3 default charset=utf8mb4;