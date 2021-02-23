package com.atguigu.educmse.service;

import com.atguigu.educmse.entity.CrmBanner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-01-21
 */
public interface CrmBannerService extends IService<CrmBanner> {
    //1.查询所有banner
    List<CrmBanner> getAllBanner();
}
