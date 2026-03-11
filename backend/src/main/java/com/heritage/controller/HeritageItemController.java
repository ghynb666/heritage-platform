package com.heritage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.dto.HeritageItemDTO;
import com.heritage.service.AccessLogService;
import com.heritage.service.HeritageItemService;
import com.heritage.vo.HeritageItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "非遗项目接口")
@RestController
@RequestMapping("/api/heritage")
public class HeritageItemController {
    @Autowired
    private HeritageItemService itemService;

    @Autowired
    private AccessLogService accessLogService;

    @ApiOperation("项目列表(分页)")
    @GetMapping("/item/list")
    public Result<Page<HeritageItemVO>> getItemList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy) {
        return Result.success(itemService.getItemList(page, size, categoryId, keyword, sortBy));
    }

    @ApiOperation("项目详情")
    @GetMapping("/item/{id}")
    public Result<HeritageItemVO> getItemDetail(@PathVariable Long id,
                                                Authentication authentication,
                                                HttpServletRequest request) {
        HeritageItemVO vo = itemService.getItemDetail(id);
        if (vo == null) {
            return Result.error("项目不存在");
        }
        Long userId = authentication == null ? null : (Long) authentication.getPrincipal();
        accessLogService.recordAccess("HERITAGE", id, userId, request);
        return Result.success(vo);
    }

    @ApiOperation("关键词搜索")
    @GetMapping("/item/search")
    public Result<Page<HeritageItemVO>> searchItems(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return Result.success(itemService.searchItems(page, size, keyword));
    }
}
