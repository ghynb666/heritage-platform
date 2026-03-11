package com.heritage.controller.admin;

import com.heritage.common.Result;
import com.heritage.entity.SysRegion;
import com.heritage.service.RegionService;
import com.heritage.vo.RegionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "管理员-地区管理")
@RestController
@RequestMapping("/api/admin/region")
public class AdminRegionController {
    @Autowired
    private RegionService regionService;

    @ApiOperation("地区树形结构")
    @GetMapping("/tree")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<List<RegionVO>> getRegionTree() {
        return Result.success(regionService.getRegionTree());
    }

    @ApiOperation("新增地区")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> addRegion(@RequestBody SysRegion region) {
        regionService.addRegion(region);
        return Result.success();
    }

    @ApiOperation("更新地区")
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> updateRegion(@RequestBody SysRegion region) {
        regionService.updateRegion(region);
        return Result.success();
    }

    @ApiOperation("删除地区")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return Result.success();
    }
}
