package org.gotomove.flashsale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gotomove.flashsale.mapper.OrderMapper;
import org.gotomove.flashsale.pojo.FlashSaleGoods;
import org.gotomove.flashsale.pojo.FlashSaleOrder;
import org.gotomove.flashsale.pojo.Order;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.service.IOrderService;
import org.gotomove.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
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
    private OrderMapper orderMapper;

    @Override
    public Order flashSale(User user, GoodsVo goodsVo) {
        // 秒杀商品表减库存
        FlashSaleGoods flashsaleGoods = flashSaleGoodsService.getOne(new QueryWrapper<FlashSaleGoods>().eq("goods_id", goodsVo.getId()));
        flashsaleGoods.setStockCount(flashsaleGoods.getStockCount() - 1);
        flashSaleGoodsService.updateById(flashsaleGoods);

        // 生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId("0L");
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(flashsaleGoods.getFlashsalePrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateTime(new Date());
        orderMapper.insert(order);

        // 生成秒杀订单
        FlashSaleOrder flashSaleOrder = new FlashSaleOrder();
        flashSaleOrder.setGoodsId(goodsVo.getId());
        flashSaleOrder.setOrderId(order.getId());
        flashSaleOrder.setUserId(user.getId());
        return order;
    }
}
