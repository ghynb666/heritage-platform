package com.heritage.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.service.ForumService;
import com.heritage.vo.ForumPostVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "管理员论坛接口")
@RestController
@RequestMapping("/api/admin/forum")
@PreAuthorize("hasRole('ADMIN')")
public class AdminForumController {
    @Autowired
    private ForumService forumService;

    @ApiOperation("帖子分页列表")
    @GetMapping("/page")
    public Result<Page<ForumPostVO>> getPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        return Result.success(forumService.getPage(page, size, status, keyword));
    }

    @ApiOperation("帖子详情")
    @GetMapping("/{id}")
    public Result<ForumPostVO> getDetail(@PathVariable Long id) {
        return Result.success(forumService.getDetail(id));
    }

    @ApiOperation("删除帖子")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        forumService.delete(id);
        return Result.success();
    }

    @ApiOperation("帖子置顶")
    @PutMapping("/{id}/top")
    public Result<?> updateTop(@PathVariable Long id, @RequestParam Integer isTop) {
        forumService.updateTop(id, isTop);
        return Result.success();
    }
}
