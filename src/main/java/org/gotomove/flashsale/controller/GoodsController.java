package org.gotomove.flashsale.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.gotomove.flashsale.pojo.Goods;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.service.IGoodsService;
import org.gotomove.flashsale.service.IUserService;
import org.gotomove.flashsale.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @Author zhang
 * @Date 2024/1/24
 * @Description
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IGoodsService iGoodsService;

    @RequestMapping("/toList")
    public String toList(Model model, User user) {
        if (user == null) {
            throw new RuntimeException("用户信息为空");
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", iGoodsService.findGoodsVo());
        return "goodsList";
    }

    /**
     * 跳转商品详情页面
     * @param GoodsId
     * @return
     */
    @RequestMapping("/toDetail/{GoodsId}")
    public String toDetail(Model model, User user, @PathVariable Long GoodsId) {
        model.addAttribute("user", user);
        // 获取货物信息
        GoodsVo goodsVo = iGoodsService.findGoodsVoByGoodsId(GoodsId);
        model.addAttribute("goods", goodsVo);
        Date startDate = goodsVo.getStartTime();
        Date endDate = goodsVo.getEndTime();
        Date nowDate = new Date();
        System.out.println(startDate + " " + endDate + " " + nowDate);
        // 秒杀状态 0 秒杀未开始，1 秒杀进行时 2 秒杀已结束
        int flashSaleStatus = 0;
        // 剩余时间
        int remainSeconds = 0;
        if (nowDate.before(startDate)) {
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 10000);
        } else if (nowDate.after(endDate)) {
            flashSaleStatus = 2;
            remainSeconds = -1;
        } else {
            flashSaleStatus = 1;
        }
        model.addAttribute("remainSeconds", remainSeconds);
        model.addAttribute("flashSaleStatus", flashSaleStatus);
        return "goodsDetail";
    }
}
