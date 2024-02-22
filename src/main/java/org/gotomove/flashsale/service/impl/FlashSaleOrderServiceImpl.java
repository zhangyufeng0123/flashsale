package org.gotomove.flashsale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gotomove.flashsale.mapper.FlashSaleOrderMapper;
import org.gotomove.flashsale.pojo.FlashSaleOrder;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.service.IFlashSaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gotomove
 * @since 2024-01-26
 */
@Service
public class FlashSaleOrderServiceImpl extends ServiceImpl<FlashSaleOrderMapper, FlashSaleOrder> implements IFlashSaleOrderService {
    @Autowired
    private FlashSaleOrderMapper flashSaleOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return 返回秒杀订单
     */
    @Override
    public Long getResult(User user, Long goodsId) {
        FlashSaleOrder flashSaleOrder = flashSaleOrderMapper.selectOne(
                new QueryWrapper<FlashSaleOrder>().eq("goods_id", goodsId)
                        .eq("user_id", user.getId())
        );
        if (flashSaleOrder != null) {
            return flashSaleOrder.getOrderId();
        } else {
            if (redisTemplate.hasKey("isSocketEmpty:" + goodsId)) {
                return -1L;
            } else {
                return 0L;
            }
        }
    }
}
