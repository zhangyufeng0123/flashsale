package org.gotomove.flashsale.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gotomove.flashsale.mapper.GoodsMapper;
import org.gotomove.flashsale.pojo.Goods;
import org.gotomove.flashsale.service.IGoodsService;
import org.gotomove.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gotomove
 * @since 2024-01-26
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    /**
     *
     * @return 商品列表
     */
    @Override
    public List<GoodsVo> findGoodsVo() {
        return goodsMapper.findGoodsVo();
    }

    @Override
    public GoodsVo findGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.findGoodsVoByGoodsId(goodsId);
    }
}
