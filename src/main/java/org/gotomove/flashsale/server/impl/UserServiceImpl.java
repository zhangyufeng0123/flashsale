package org.gotomove.flashsale.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gotomove.flashsale.mapper.UserMapper;
import org.gotomove.flashsale.pojo.User;
import org.gotomove.flashsale.service.IUserService;
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

}
