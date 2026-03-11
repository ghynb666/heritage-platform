package com.heritage.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.dto.UserQueryDTO;
import com.heritage.service.InheritorService;
import com.heritage.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "管理员-传承人管理")
@RestController
@RequestMapping("/api/admin/inheritor")
public class AdminInheritorController {
    @Autowired
    private InheritorService inheritorService;

    @ApiOperation("传承人分页列表")
    @GetMapping("/page")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Page<UserVO>> getInheritorPage(UserQueryDTO query) {
        return Result.success(inheritorService.getInheritorPage(query));
    }

    @ApiOperation("传承人详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<UserVO> getInheritorDetail(@PathVariable Long id) {
        return Result.success(inheritorService.getInheritorDetail(id));
    }

    @ApiOperation("修改非遗类型")
    @PutMapping("/{id}/category")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> updateCategory(@PathVariable Long id, @RequestParam Long categoryId) {
        inheritorService.updateCategory(id, categoryId);
        return Result.success();
    }

    @ApiOperation("取消传承人资格")
    @PutMapping("/{id}/revoke")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> revokeInheritor(@PathVariable Long id) {
        inheritorService.revokeInheritor(id);
        return Result.success();
    }
}
