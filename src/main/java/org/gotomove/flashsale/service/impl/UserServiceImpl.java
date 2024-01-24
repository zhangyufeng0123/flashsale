package org.gotomove.flashsale.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.gotomove.flashsale.exception.GlobalException;
import org.gotomove.flashsale.mapper.UserMapper;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.service.IUserService;
import org.gotomove.flashsale.utils.CookieUtil;
import org.gotomove.flashsale.utils.MD5Util;
import org.gotomove.flashsale.utils.UUIDUtil;
import org.gotomove.flashsale.utils.ValidatorUtil;
import org.gotomove.flashsale.vo.LoginVo;
import org.gotomove.flashsale.vo.RespBean;
import org.gotomove.flashsale.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    // 登录
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        log.info("{}", loginVo);
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        // 参数校验
//        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        if (!ValidatorUtil.isMobile(mobile)) {
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
        User user = userMapper.selectById(mobile);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        String pwd = user.getPassword();
        String salt = user.getSalt();
        // 判断密码是否正确
        if (!pwd.equals(MD5Util.fromPassToDBPass(password, salt))) {
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);

        }
        // 生成cookie值
        String ticket = UUIDUtil.uuid();
        request.getSession().setAttribute(ticket, user);
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return RespBean.success();
    }
}
