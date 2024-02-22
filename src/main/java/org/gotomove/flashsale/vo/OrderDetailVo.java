package org.gotomove.flashsale.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gotomove.flashsale.pojo.Order;

/**
 * @Author zhang
 * @Date 2024/2/5
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailVo {
    private Order order;
    private GoodsVo goodsVo;
}
