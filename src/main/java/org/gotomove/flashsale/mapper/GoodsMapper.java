package org.gotomove.flashsale.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.gotomove.flashsale.pojo.Goods;
import org.gotomove.flashsale.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author gotomove
 * @since 2024-01-26
 */
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 获取商品列表
     * @return
     */
    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
