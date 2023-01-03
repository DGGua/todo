package todo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import todo.entity.User;
import todo.mapper.UserMapper;
import todo.service.UserService;

/**
 * @ClassName:UserImpl
 * @Description:用户实现类
 * @Author:liumengying
 * @Date: 2023/1/1 15:10
 * Version v1.0
 */
@Service
public class UserImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
