package org.gotomove.flashsale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.gotomove.flashsale.pojo.Goods;
import org.gotomove.flashsale.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gotomove
 * @since 2024-01-26
 */
public interface IGoodsService extends IService<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
