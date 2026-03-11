package com.heritage.controller.admin;

import com.heritage.common.Result;
import com.heritage.dto.BannerDTO;
import com.heritage.dto.SortItemDTO;
import com.heritage.entity.Banner;
import com.heritage.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "管理员轮播图接口")
@RestController
@RequestMapping("/api/admin/banner")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBannerController {
    @Autowired
    private BannerService bannerService;

    @ApiOperation("轮播图列表")
    @GetMapping("/list")
    public Result<List<Banner>> getList() {
        return Result.success(bannerService.getList());
    }

    @ApiOperation("新增轮播图")
    @PostMapping
    public Result<?> add(@Validated @RequestBody BannerDTO dto) {
        bannerService.add(dto);
        return Result.success();
    }

    @ApiOperation("更新轮播图")
    @PutMapping
    public Result<?> update(@Validated @RequestBody BannerDTO dto) {
        bannerService.update(dto);
        return Result.success();
    }

    @ApiOperation("删除轮播图")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return Result.success();
    }

    @ApiOperation("更新轮播图排序")
    @PutMapping("/sort")
    public Result<?> updateSort(@RequestBody List<SortItemDTO> items) {
        bannerService.updateSort(items);
        return Result.success();
    }
}
