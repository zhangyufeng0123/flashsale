package org.gotomove.flashsale.controller;


import org.gotomove.flashsale.pojo.Order;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.service.IOrderService;
import org.gotomove.flashsale.vo.OrderDetailVo;
import org.gotomove.flashsale.vo.RespBean;
import org.gotomove.flashsale.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gotomove
 * @since 2024-01-26
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired private IOrderService iOrderService;

    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo orderDetailVo = iOrderService.detail(orderId);
        return RespBean.success(orderDetailVo);
    }
}
