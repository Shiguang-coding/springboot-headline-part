package com.atguigu.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.atguigu.pojo.User;
import com.atguigu.utils.JwtHelper;
import com.atguigu.utils.MD5Util;
import com.atguigu.utils.Result;
import com.atguigu.utils.ResultCodeEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.service.UserService;
import com.atguigu.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ShiGuang
 * @description 针对表【news_user】的数据库操作Service实现
 * @createDate 2024-09-10 09:39:58
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtHelper jwtHelper;


    /**
     * 登录业务
     * 1.根据账号，查询用户对象-LoginUser
     * 2.如果用户对象为null,查询失败，账号错误！501
     * 3.对比，密码，失败返回503的错误
     * 4.根据用户id生成一个token,token->result返回
     *
     * @param user
     * @return Result
     */
    @Override
    public Result login(User user) {

        // 根据账号查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUsername, user.getUsername());
        User loginUser = userMapper.selectOne(queryWrapper);

        // 用户不存在
        if (loginUser == null) {
            return Result.ok("", ResultCodeEnum.USERNAME_ERROR);
        }

        // 对比密码
        if (!StringUtils.isEmpty(user.getUserPwd())
                && MD5Util.encrypt(user.getUserPwd()).equals(loginUser.getUserPwd())) {
            // 登录成功
            // 根据用户ID生成token
            String token = jwtHelper.createToken(Long.valueOf(loginUser.getUid()));

            //将token封装到Result返回
            Map data = new HashMap();
            data.put("token", token);

            return Result.ok(data);
        } else {
            // 密码错误
            return Result.build("", ResultCodeEnum.PASSWORD_ERROR);
        }
    }

    /**
     * 根据token获取用户数据
     * 1.token是否在有效蝴
     * 2.根据token解析userId
     * 3.根据用户id查询用数据
     * 4.去掉密码，封装result结果返回即可
     * @param token
     * @return
     */
    @Override
    public Result getUserInfo(String token) {
        // 判断token是否过期
        boolean expiration = jwtHelper.isExpiration(token);
        if (expiration){// token失效
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }

        int userId = jwtHelper.getUserId(token).intValue();

        User user = userMapper.selectById(userId);

        if (user == null){
            return Result.build(null,ResultCodeEnum.NOTLOGIN);
        }

        user.setUserPwd(null);

        Map data = new HashMap();
        data.put("loginUser",user);

        return Result.ok(data);
    }


    /**
     * 检查账号是否可以注册
     *
     * @param username 账号信息
     * @return
     */
    @Override
    public Result checkUserName(String username) {

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();


        queryWrapper.eq(User::getUsername,username);
//        int count = userMapper.selectCount(queryWrapper).intValue();
//        if (count == 1){
//            return Result.build(null,ResultCodeEnum.USERNAME_USED);
//        }
        User user = userMapper.selectOne(queryWrapper);

        if (user != null){
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }

        return Result.ok(null);
    }

    @Override
    public Result regist(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        Long count = userMapper.selectCount(queryWrapper);

        if (count > 0){
            return Result.build(null,ResultCodeEnum.USERNAME_USED);
        }

        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        int rows = userMapper.insert(user);
        System.out.println("rows = " + rows);
        return Result.ok(null);
    }
}




