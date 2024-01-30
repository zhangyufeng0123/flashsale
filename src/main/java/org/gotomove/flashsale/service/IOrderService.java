package org.gotomove.flashsale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.gotomove.flashsale.pojo.Goods;
import org.gotomove.flashsale.pojo.Order;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gotomove
 * @since 2024-01-26
 */
public interface IOrderService extends IService<Order> {
    Order flashSale(User user, GoodsVo goodsVo);
}
