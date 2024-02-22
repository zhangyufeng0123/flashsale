package org.gotomove.flashsale.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gotomove.flashsale.pojo.User;

/**
 * @Author zhang
 * @Date 2024/2/21
 * @Description 秒杀信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashSaleMessage {
    private User user;

    private Long goodsId;
}
