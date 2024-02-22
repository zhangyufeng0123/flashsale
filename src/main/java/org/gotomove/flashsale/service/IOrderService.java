package org.gotomove.flashsale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.gotomove.flashsale.pojo.Goods;
import org.gotomove.flashsale.pojo.Order;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.vo.GoodsVo;
import org.gotomove.flashsale.vo.OrderDetailVo;

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

    OrderDetailVo detail(Long orderId);

    boolean checkPath(User user, Long goodsId, String path);

    String createPath(User user, Long goodsId);

    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
