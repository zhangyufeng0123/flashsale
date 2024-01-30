package org.gotomove.flashsale.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.gotomove.flashsale.exception.GlobalException;
import org.gotomove.flashsale.mapper.UserMapper;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.service.IUserService;
import org.gotomove.flashsale.utils.CookieUtil;
import org.gotomove.flashsale.utils.MD5Util;
import org.gotomove.flashsale.utils.UUIDUtil;
import org.gotomove.flashsale.vo.LoginVo;
import org.gotomove.flashsale.vo.RespBean;
import org.gotomove.flashsale.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gotomove
 * @since 2024-01-16
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        User user = userMapper.selectById(mobile);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        if (!MD5Util.fromPassToDBPass(password, user.getSalt()).equals(user.getPassword())) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        String userTicket = UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:" + userTicket, user);
        CookieUtil.setCookie(request, response, "userTicket", userTicket);
        return RespBean.success(userTicket);
    }

    @Override
    public User getUserByCookie(HttpServletRequest request, HttpServletResponse response, String userTicket) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if (user!= null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }
}
