package org.gotomove.flashsale.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.servlet.http.HttpSession;
import org.gotomove.flashsale.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author zhang
 * @Date 2024/1/24
 * @Description
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    /**
     * 跳转商品页面
     * @param session
     * @param model
     * @param ticket cookie值
     * @return
     */
    @RequestMapping("/toList")
    public String toList(HttpSession session, Model model, @CookieValue("userTicket") String ticket) {
        // 判断cookie 是否为空
        if (StringUtils.isEmpty(ticket)) {
            return "login";
        }
        User user = (User) session.getAttribute(ticket);
        if (user == null) {
            return "login";
        }
        // 将用户信息传到前端页面中
        model.addAttribute("user", user);
        return "goodsList";
    }
}
