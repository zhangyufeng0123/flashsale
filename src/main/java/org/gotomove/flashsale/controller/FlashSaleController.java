package org.gotomove.flashsale.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.gotomove.flashsale.pojo.FlashSaleOrder;
import org.gotomove.flashsale.pojo.Order;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.service.IGoodsService;
import org.gotomove.flashsale.service.impl.FlashSaleOrderServiceImpl;
import org.gotomove.flashsale.service.impl.OrderServiceImpl;
import org.gotomove.flashsale.vo.GoodsVo;
import org.gotomove.flashsale.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author zhang
 * @Date 2024/1/29
 * @Description
 */
@Controller
@RequestMapping("/flashSale")
public class FlashSaleController {
    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private FlashSaleOrderServiceImpl flashSaleOrderService;

    @Autowired
    private OrderServiceImpl orderService;

    @RequestMapping("/doFlashSale")
    public String doFlashSale(Model model, User user, Long goodsId) {
        // 如果未登录，则返回到登录界面
        if (user == null) {
            return "login";
        }
        // 添加用户信息
        model.addAttribute("user", user);
        GoodsVo goodsVo = iGoodsService.findGoodsVoByGoodsId(goodsId);
        // 判断库存
        if (goodsVo.getStockCount() <= 0) {
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK.getMessage());
            return "flashSaleFail";
        }
        // 判断是否重复抢购
        FlashSaleOrder flashSaleOrder = flashSaleOrderService.getOne(new QueryWrapper<FlashSaleOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
        if (flashSaleOrder != null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATE_ERROR.getMessage());
            return "flashSaleFail";
        }
        Order order = orderService.flashSale(user, goodsVo);
        model.addAttribute("order", order);
        model.addAttribute("goods", goodsVo);
        return "orderDetail";
    }
}
