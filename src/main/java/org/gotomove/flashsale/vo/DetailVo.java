package org.gotomove.flashsale.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gotomove.flashsale.pojo.User;

/**
 * @Author zhang
 * @Date 2024/2/3
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVo {
    private User user;
    private GoodsVo goodsVo;
    private int flashSaleStatus;
    private int remainSeconds;
}
