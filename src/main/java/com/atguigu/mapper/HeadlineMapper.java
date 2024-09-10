package com.atguigu.mapper;

import com.atguigu.pojo.Headline;
import com.atguigu.vo.PortalVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author ShiGuang
 * @description 针对表【news_headline】的数据库操作Mapper
 * @createDate 2024-09-10 09:39:58
 * @Entity com.atguigu.pojo.Headline
 */

@Repository
public interface HeadlineMapper extends BaseMapper<Headline> {
    //自定义分页查询方法
    IPage<Map> selectPageMap(IPage<Headline> page,
                             @Param("portalVo") PortalVo portalVo);

    /**
     * 分页查询头条详情
     *
     * @param hid
     * @return
     */
    Map selectDetailMap(Integer hid);
}




