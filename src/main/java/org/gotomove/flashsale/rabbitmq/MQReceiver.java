package org.gotomove.flashsale.rabbitmq;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.gotomove.flashsale.pojo.FlashSaleOrder;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.service.impl.GoodsServiceImpl;
import org.gotomove.flashsale.service.impl.OrderServiceImpl;
import org.gotomove.flashsale.vo.FlashSaleMessage;
import org.gotomove.flashsale.vo.GoodsVo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author zhang
 * @Date 2024/2/21
 * @Description
 */
@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private GoodsServiceImpl goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderServiceImpl orderService;
//    @RabbitListener(queues = "queue")
//    public void receive(Object msg) {
//        log.info("接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue01")
//    public void receive01(Object msg) {
//        log.info("queue01接收消息：" + msg);
//    }
//
//    @RabbitListener(queues = "queue02")
//    public void receive02(Object msg) {
//        log.info("queue02接收消息：" + msg);
//    }
    @RabbitListener(queues = "flashSaleQueue")
    public void receive(String msg) {
        log.info("QUEUE接收消息：" + msg);
        FlashSaleMessage flashSaleMessage = JSON.parseObject(msg, FlashSaleMessage.class);
        Long goodsId = flashSaleMessage.getGoodsId();
        User user = flashSaleMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            return;
        }
        // 判断是否重复购买
        FlashSaleOrder flashSaleOrder = (FlashSaleOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (flashSaleOrder != null) {
            return;
        }
        // 下单操作
        orderService.flashSale(user, goodsVo);
    }
}
