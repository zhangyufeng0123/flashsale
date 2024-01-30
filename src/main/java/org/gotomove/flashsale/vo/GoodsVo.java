package org.gotomove.flashsale.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gotomove.flashsale.pojo.Goods;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author zhang
 * @Date 2024/1/26
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo extends Goods {

    private BigDecimal flashsalePrice;

    private Integer stockCount;

    private Date startTime;

    private Date endTime;
}
