package org.gotomove.flashsale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.gotomove.flashsale.pojo.FlashSaleOrder;
import org.gotomove.flashsale.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gotomove
 * @since 2024-01-26
 */
public interface IFlashSaleOrderService extends IService<FlashSaleOrder> {
    Long getResult(User user, Long goodsId);
}
