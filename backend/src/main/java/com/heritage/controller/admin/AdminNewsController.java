package com.heritage.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.dto.NewsDTO;
import com.heritage.entity.News;
import com.heritage.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "管理员资讯接口")
@RestController
@RequestMapping("/api/admin/news")
@PreAuthorize("hasRole('ADMIN')")
public class AdminNewsController {
    @Autowired
    private NewsService newsService;

    @ApiOperation("资讯分页列表")
    @GetMapping("/page")
    public Result<Page<News>> getPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        return Result.success(newsService.getPage(page, size, status, keyword));
    }

    @ApiOperation("资讯详情")
    @GetMapping("/{id}")
    public Result<News> getDetail(@PathVariable Long id) {
        return Result.success(newsService.getDetail(id));
    }

    @ApiOperation("新增资讯")
    @PostMapping
    public Result<?> add(@Validated @RequestBody NewsDTO dto, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        newsService.add(dto, userId);
        return Result.success();
    }

    @ApiOperation("更新资讯")
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @Validated @RequestBody NewsDTO dto) {
        newsService.update(id, dto);
        return Result.success();
    }

    @ApiOperation("删除资讯")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        newsService.delete(id);
        return Result.success();
    }

    @ApiOperation("审核资讯")
    @PutMapping("/{id}/audit")
    public Result<?> audit(@PathVariable Long id,
                           @RequestParam Integer status,
                           @RequestParam(required = false) String auditReason) {
        newsService.audit(id, status, auditReason);
        return Result.success();
    }

    @ApiOperation("资讯置顶")
    @PutMapping("/{id}/top")
    public Result<?> updateTop(@PathVariable Long id, @RequestParam Integer isTop) {
        newsService.updateTop(id, isTop);
        return Result.success();
    }
}
