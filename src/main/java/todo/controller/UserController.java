package todo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import todo.common.R;
import todo.entity.User;
import todo.service.UserService;

import javax.servlet.http.HttpSession;

/**
 * @ClassName:UserController
 * @Description:用户控制器类
 * @Author:liumengying
 * @Date: 2023/1/1 15:24
 * Version v1.0
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody User user,HttpSession session ){
       //查询数据库
        final LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserID,user.getUserID());
        final User user1=  userService.getOne(queryWrapper);
        //没有根据账号找到数据
        if(user1 == null){
            return R.error("账号不存在");
        }
        //密码比对
        if(!user1.getPassword().equals(user.getPassword())){
            return R.error("密码错误");
        }
        //登陆成功,保存userID
        session.setAttribute("userID",user.getUserID());
        System.out.println("实验，查看session保存的ID:"+(String) session.getAttribute("userID"));
        return R.success(user1);

    }

    @PostMapping("/register")
    public R<String> register(@RequestBody User user){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserID,user.getUserID());
        User user1 = userService.getOne(queryWrapper);
        if(user1!=null){
            return R.error("账号已存在");
        }
        userService.save(user);
        return R.success("注册成功");
    }
}
