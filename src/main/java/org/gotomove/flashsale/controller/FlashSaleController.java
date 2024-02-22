package org.gotomove.flashsale.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.springboot.captcha.SpecCaptcha;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.gotomove.flashsale.exception.GlobalException;
import org.gotomove.flashsale.pojo.FlashSaleGoods;
import org.gotomove.flashsale.pojo.FlashSaleOrder;
import org.gotomove.flashsale.pojo.Order;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.rabbitmq.MQSender;
import org.gotomove.flashsale.service.IGoodsService;
import org.gotomove.flashsale.service.impl.FlashSaleGoodsServiceImpl;
import org.gotomove.flashsale.service.impl.FlashSaleOrderServiceImpl;
import org.gotomove.flashsale.service.impl.OrderServiceImpl;
import org.gotomove.flashsale.vo.FlashSaleMessage;
import org.gotomove.flashsale.vo.GoodsVo;
import org.gotomove.flashsale.vo.RespBean;
import org.gotomove.flashsale.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhang
 * @Date 2024/1/29
 * @Description
 */
@Controller
@Slf4j
@RequestMapping("/flashSale")
public class FlashSaleController implements InitializingBean {
    @Autowired
    private IGoodsService iGoodsService;

    @Autowired
    private FlashSaleOrderServiceImpl flashSaleOrderService;

    @Autowired
    private FlashSaleGoodsServiceImpl flashSaleGoodsService;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    private Map<Long, Boolean> EmptyStockMap = new HashMap<>();

//    @RequestMapping(value = "/doFlashSale", method = RequestMethod.POST)
//    public RespBean doFlashSale(User user, Long goodsId) {
//        // 如果未登录，则返回到登录界面
//        if (user == null) {
//            return RespBean.error(RespBeanEnum.SESSION_ERROR);
//        }
//        // 添加用户信息
//        GoodsVo goodsVo = iGoodsService.findGoodsVoByGoodsId(goodsId);
//        // 判断库存
//        if (goodsVo.getStockCount() <= 0) {
//            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
//        }
//        // 判断是否重复抢购
//        FlashSaleOrder flashSaleOrder = flashSaleOrderService.getOne(new QueryWrapper<FlashSaleOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId));
//        if (flashSaleOrder != null) {
//            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
//        }
//        Order order = orderService.flashSale(user, goodsVo);
//        return RespBean.success(order);
//    }
    @RequestMapping(value="/{path}/doFlashSale", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doFlashSale(@PathVariable String path, User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
//        GoodsVo goodsVo = iGoodsService.findGoodsVoByGoodsId(goodsId);
//        // 判断库存
//        if (goodsVo.getStockCount() < 1) {
//            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
//        }
//        String flashSaleOrderJson = (String) redisTemplate.opsForValue()
//                .get("order:" + user.getId() + ":" + goodsId);
//        if (!StringUtils.isEmpty(flashSaleOrderJson)) {
//            return RespBean.error((RespBeanEnum.REPEAT_ERROR));
//        }
//        Order order = orderService.flashSale(user, goodsVo);
//        if (order != null) {
//            return RespBean.success(order);
//        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        boolean checkValue = orderService.checkPath(user, goodsId, path);
        if (!checkValue) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        // 判断是否重复抢购
        FlashSaleOrder flashSaleOrderJson = (FlashSaleOrder) valueOperations.get("order:" + user.getId() + ":" + goodsId);
        if (flashSaleOrderJson != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        // 内存标记，减少redis访问
        if (EmptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 预减库存
        Long stock = valueOperations.decrement("flashSaleGoods:" + goodsId);
        if (stock < 0) {
            EmptyStockMap.put(goodsId, Boolean.TRUE);
            valueOperations.increment("flashSaleGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        // 请求入队，立即返回派对中
        FlashSaleMessage flashSaleMessage = new FlashSaleMessage(user, goodsId);
        mqSender.sendFlashSaleMessage(JSON.toJSONString(flashSaleMessage));
        return RespBean.success(0);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = iGoodsService.findGoodsVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("flashSaleGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            EmptyStockMap.put(goodsVo.getId(), false);
        });
    }

    @RequestMapping(value="/result", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        Long orderId = flashSaleOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }

    /**
     * 获取秒杀地址
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        String str = orderService.createPath(user, goodsId);
        return RespBean.success(str);
    }

    @RequestMapping(value = "/captcha", method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response) {
        if (null == user || goodsId < 0) {
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }

        // 设置请求头为输出图片类型
        response.setContentType("image/jpg");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 生成验证码，结果放到redis中
        SpecCaptcha captcha = new SpecCaptcha(130, 32);
        redisTemplate.opsForValue().set("captcha:" + user.getId() + ":" + goodsId, captcha.text(), 300, TimeUnit.SECONDS);
        try{
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败", e.getMessage());
        }
    }
}
