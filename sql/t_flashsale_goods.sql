create table `t_flashsale_goods`(
	`id` bigint(20) not null auto_increment comment '秒杀商品ID',
	`goods_id` bigint(20) default null comment '商品ID',
	`flashsale_price` decimal(10, 2) default '0.00' comment '秒杀价格',
	`stock_count` int(10) default null comment '库存数量',
	`start_time` datetime default null comment '秒杀开始时间',
	`end_time` datetime default null comment '秒杀结束时间',
	primary key(`id`)
) engine=innodb auto_increment=3 default charset=utf8mb4;