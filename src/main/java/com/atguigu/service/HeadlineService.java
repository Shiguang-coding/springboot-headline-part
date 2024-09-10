package com.atguigu.service;

import com.atguigu.pojo.Headline;
import com.atguigu.utils.Result;
import com.atguigu.vo.PortalVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ShiGuang
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2024-09-10 09:39:58
*/
public interface HeadlineService extends IService<Headline> {

    Result findNewsPage(PortalVo portalVo);

    Result showHeadlineDetail(Integer hid);

    Result publish(Headline headline);

    Result findHeadlineByHid(Integer hid);

    Result updateHeadLine(Headline headline);
}
