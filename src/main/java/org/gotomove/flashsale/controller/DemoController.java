package org.gotomove.flashsale.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author zhang
 * @Date 2024/1/16
 * @Description
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name", "gotomove");
        return "hello";
    }
}
