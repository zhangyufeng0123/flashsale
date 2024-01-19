package org.gotomove.flashsale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.vo.LoginVo;
import org.gotomove.flashsale.vo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gotomove
 * @since 2024-01-16
 */
public interface IUserService extends IService<User> {

    // 登录
    RespBean doLogin(LoginVo loginVo);
}
