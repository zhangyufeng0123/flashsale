package org.gotomove.flashsale.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.gotomove.flashsale.pojo.Goods;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.service.IGoodsService;
import org.gotomove.flashsale.service.IUserService;
import org.gotomove.flashsale.vo.DetailVo;
import org.gotomove.flashsale.vo.GoodsVo;
import org.gotomove.flashsale.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping("/toList2")
    public String toList2(Model model, User user) {
        if (user == null) {
            throw new RuntimeException("用户信息为空");
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", iGoodsService.findGoodsVo());
        return "goodsList";
    }

    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8", method = RequestMethod.GET)
    @ResponseBody
    public String toList(HttpServletRequest request, HttpServletResponse response, Model model, User user) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // Redis 中获取页面，如果不为空，直接返回页面
        String html = (String) valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        model.addAttribute("goodsList", iGoodsService.findGoodsVo());
        // 如果为空，手动渲染，存入Redis并返回
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(request.getServletContext()).buildExchange(request, response);
        WebContext wct = new WebContext(webExchange, request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList", wct);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsList", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "/toDetail2/{goodsId}", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(HttpServletRequest request, HttpServletResponse response, Model model, User user, @PathVariable Long goodsId) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // 从Redis中获取页面，如果不为空，则直接返回
        String html = (String) valueOperations.get("goodsDetail:" + goodsId);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }
        model.addAttribute("user", user);
        GoodsVo goodsVo = iGoodsService.findGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goodsVo);
        Date startDate = goodsVo.getStartTime();
        Date endDate = goodsVo.getEndTime();
        Date nowDate = new Date();

        // 秒杀状态
        int flashSaleStatus = 0;
        // 剩余开始时间
        int remainSeconds = 0;
        // 秒杀还未开始
        if (nowDate.before(startDate)) {
            remainSeconds = (int) ((startDate.getTime() - nowDate.getTime()) / 1000);
        } else if (nowDate.after(endDate)) {
            // 秒杀已结束
            flashSaleStatus = 2;
            remainSeconds = -1;
        } else {
            // 秒杀进行中
            flashSaleStatus = 1;
        }
        model.addAttribute("flashSaleStatus", flashSaleStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        // 如果为空，手动渲染，存入Redis并返回
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(request.getServletContext()).buildExchange(request, response);
        WebContext wct = new WebContext(webExchange, request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", wct);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsDetail", html, 60, TimeUnit.SECONDS);
        }
        return html;
    }

    /**
     * 跳转商品详情页面
     *
     * @param goodsId
     * @return
     */
    @RequestMapping("/detail/{goodsId}")
    @ApiOperation("商品详情")
    @ResponseBody
    public RespBean toDetail(Model model, User user, @PathVariable Long goodsId) {
        // 获取货物信息
        GoodsVo goodsVo = iGoodsService.findGoodsVoByGoodsId(goodsId);
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
        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setRemainSeconds(remainSeconds);
        detailVo.setFlashSaleStatus(flashSaleStatus);
        return RespBean.success(detailVo);
    }
}
