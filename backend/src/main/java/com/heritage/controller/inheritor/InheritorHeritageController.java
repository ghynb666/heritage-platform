package com.heritage.controller.inheritor;

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

@Api(tags = "传承人非遗项目接口")
@RestController
@RequestMapping("/api/inheritor/heritage")
@PreAuthorize("hasAnyRole('INHERITOR', 'ADMIN')")
public class InheritorHeritageController {
    @Autowired
    private HeritageItemService itemService;

    @ApiOperation("发布项目")
    @PostMapping("/item")
    public Result<?> publishItem(@RequestBody HeritageItemDTO dto, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        itemService.publishItem(dto, userId, 1);
        return Result.success();
    }

    @ApiOperation("编辑项目")
    @PutMapping("/item")
    public Result<?> updateItem(@RequestBody HeritageItemDTO dto, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        itemService.updateItem(dto, userId);
        return Result.success();
    }

    @ApiOperation("删除项目")
    @DeleteMapping("/item/{id}")
    public Result<?> deleteItem(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        itemService.deleteItem(id, userId);
        return Result.success();
    }

    @ApiOperation("我的项目")
    @GetMapping("/item/list")
    public Result<Page<HeritageItemVO>> getMyItems(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(itemService.getMyItems(page, size, userId));
    }
}
