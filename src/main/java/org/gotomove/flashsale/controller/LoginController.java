package org.gotomove.flashsale.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhang
 * @Date 2024/1/16
 * @Description
 */
@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }
}
