package com.atguigu.educmse.controller;


import com.atguigu.commonutils.R;
import com.atguigu.educmse.entity.CrmBanner;
import com.atguigu.educmse.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-01-21
 */
@RestController
@RequestMapping("/educmse/banneradmin")
@CrossOrigin
public class BannerAdminController {

    /*@Autowired
    private CrmBannerService bannerService;

    //1.分页查询banner
    @GetMapping("pageBanner/{page}/{limit}")
    public R pageBanner(@PathVariable long page, @PathVariable long limit){
        Page<CrmBanner> pageBanner = new Page<>(page,limit);
        bannerService.page(pageBanner,null);
        return R.ok().data("items",pageBanner.getRecords()).data("total",pageBanner.getTotal());
    }

    //2.添加banner
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        bannerService.save(crmBanner);
        return R.ok();
    }

    //3.获取banner
    @ApiOperation(value = "获取Banner")
    @GetMapping("get/${id}")
    public R get(@PathVariable String id){
        CrmBanner crmBanner = bannerService.getById(id);
        return R.ok().data("items",crmBanner);
    }

    //3.修改banner
    @ApiOperation(value = "修改Banner")
    @PutMapping("update")
    public R updateBanner(@RequestBody CrmBanner crmBanner){
        bannerService.updateById(crmBanner);
        return R.ok();
    }

    //4.删除banner
    @ApiOperation(value = "删除Banner")
    @DeleteMapping("remove/${id}")
    public R deleteBanner(@PathVariable String id){
        bannerService.removeById(id);
        return R.ok();
    }*/

}

