package org.gotomove.flashsale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gotomove.flashsale.exception.GlobalException;
import org.gotomove.flashsale.mapper.FlashSaleOrderMapper;
import org.gotomove.flashsale.mapper.OrderMapper;
import org.gotomove.flashsale.pojo.FlashSaleGoods;
import org.gotomove.flashsale.pojo.FlashSaleOrder;
import org.gotomove.flashsale.pojo.Order;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.service.IOrderService;
import org.gotomove.flashsale.utils.MD5Util;
import org.gotomove.flashsale.utils.UUIDUtil;
import org.gotomove.flashsale.vo.GoodsVo;
import org.gotomove.flashsale.vo.OrderDetailVo;
import org.gotomove.flashsale.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gotomove
 * @since 2024-01-26
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private FlashSaleGoodsServiceImpl flashSaleGoodsService;

    @Autowired
    private FlashSaleOrderMapper flashSaleOrderMapper;

    @Autowired
    private FlashSaleOrderServiceImpl flashSaleOrderService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private GoodsServiceImpl goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    //    @Override
//    public Order flashSale(User user, GoodsVo goodsVo) {
//        // 秒杀商品表减库存
////        FlashSaleGoods flashsaleGoods = flashSaleGoodsService.getOne(new QueryWrapper<FlashSaleGoods>().eq("goods_id", goodsVo.getId()));
////        flashsaleGoods.setStockCount(flashsaleGoods.getStockCount() - 1);
////        flashSaleGoodsService.updateById(flashsaleGoods);
//        FlashSaleGoods flashSaleGoods = flashSaleGoodsService.getOne(new QueryWrapper<FlashSaleGoods>().eq("goods_id", goodsVo.getId()));
//        flashSaleGoodsService.update(new UpdateWrapper<FlashSaleGoods>().set("stock_count", flashSaleGoods.getStockCount()).eq("id", flashSaleGoods.getId()).gt("stock_count", 0));
//
//        // 生成订单
//        Order order = new Order();
//        order.setUserId(user.getId());
//        order.setGoodsId(goodsVo.getId());
//        order.setDeliveryAddrId("0L");
//        order.setGoodsName(goodsVo.getGoodsName());
//        order.setGoodsCount(1);
//        order.setGoodsPrice(flashSaleGoods.getFlashsalePrice());
//        order.setOrderChannel(1);
//        order.setStatus(0);
//        order.setCreateTime(new Date());
//        orderMapper.insert(order);
//
//        // 生成秒杀订单
//        FlashSaleOrder flashSaleOrder = new FlashSaleOrder();
//        flashSaleOrder.setGoodsId(goodsVo.getId());
//        flashSaleOrder.setOrderId(order.getId());
//        flashSaleOrder.setUserId(user.getId());
//        flashSaleOrderMapper.insert(flashSaleOrder);
//        return order;
//    }
    @Override
    @Transactional
    public Order flashSale(User user, GoodsVo goodsVo) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 秒杀商品表减库存
        FlashSaleGoods flashSaleGoods = flashSaleGoodsService.getOne(new QueryWrapper<FlashSaleGoods>().eq("goods_id", goodsVo.getId()));
        flashSaleGoods.setStockCount(flashSaleGoods.getStockCount() - 1);
        boolean flashSaleGoodsResult = flashSaleGoodsService.update(new UpdateWrapper<FlashSaleGoods>().
                setSql("stock_count = " + "stock_count - 1").
                eq("goods_id", goodsVo.getId()).
                gt("stock_count", 0));
        if (!flashSaleGoodsResult) {
            return null;
        }
//        if (flashSaleGoods.getStockCount() < 1) {
//            // 将库存置为0
//            valueOperations.set("isStockEmpty:" + goodsVo.getId(), "0");
//            return null;
//        }
        // 生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId("OL");
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goodsVo.getGoodsPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateTime(new Date());
        orderMapper.insert(order);

        // 生成秒杀订单
        FlashSaleOrder flashSaleOrder = new FlashSaleOrder();
        flashSaleOrder.setOrderId(order.getId());
        flashSaleOrder.setGoodsId(order.getGoodsId());
        flashSaleOrder.setUserId(order.getUserId());
        try {
            flashSaleOrderService.save(flashSaleOrder);
            redisTemplate.opsForValue().set("order:" + user.getId() + ":" +
                            goodsVo.getId(),
                    flashSaleOrder, 1, TimeUnit.HOURS);
        } catch (DuplicateKeyException e) {
            log.error("存在重复购买");
            orderMapper.deleteById(order.getId());
        }
        return null;
    }

    @Override
    public OrderDetailVo detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setGoodsVo(goodsVo);
        return orderDetailVo;
    }

    /**
     * 验证请求地址
     *
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (user == null || StringUtils.isEmpty(path)) {
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("flashSalePath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    /**
     * 生成秒杀地址
     *
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public String createPath(User user, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("flashSalePath:" + user.getId() + ":" + goodsId, str, 1, TimeUnit.MINUTES);
        return str;
    }

    /**
     * 校验验证码
     * @param user
     * @param goodsId
     * @param captcha
     * @return
     */
    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if (StringUtils.isEmpty(captcha) || user == null || goodsId < 0) {
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        assert redisCaptcha != null;
        return redisCaptcha.equals(captcha);
    }
}
