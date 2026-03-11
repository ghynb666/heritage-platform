package com.heritage.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.dto.HeritageItemDTO;
import com.heritage.service.HeritageItemService;
import com.heritage.vo.HeritageItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Api(tags = "管理员非遗项目接口")
@RestController
@RequestMapping("/api/admin/heritage")
@PreAuthorize("hasRole('ADMIN')")
public class AdminHeritageController {
    @Autowired
    private HeritageItemService itemService;

    @ApiOperation("管理员发布项目")
    @PostMapping("/item")
    public Result<?> publishItem(@RequestBody HeritageItemDTO dto, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        itemService.publishItem(dto, userId, 0);
        return Result.success();
    }

    @ApiOperation("管理列表")
    @GetMapping("/item/list")
    public Result<Page<HeritageItemVO>> getAdminItemList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        return Result.success(itemService.getAdminItemList(page, size, status));
    }

    @ApiOperation("上下架")
    @PutMapping("/item/{id}/status")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        itemService.updateStatus(id, status);
        return Result.success();
    }
}
