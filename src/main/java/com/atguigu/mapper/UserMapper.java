package com.atguigu.mapper;

import com.atguigu.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author ShiGuang
* @description 针对表【news_user】的数据库操作Mapper
* @createDate 2024-09-10 09:39:58
* @Entity com.atguigu.pojo.User
*/
@Repository
public interface UserMapper extends BaseMapper<User> {

}




