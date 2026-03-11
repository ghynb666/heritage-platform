package com.heritage.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.service.CommentService;
import com.heritage.vo.CommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "管理员评论接口")
@RestController
@RequestMapping("/api/admin/comment")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCommentController {
    @Autowired
    private CommentService commentService;

    @ApiOperation("评论分页列表")
    @GetMapping("/page")
    public Result<Page<CommentVO>> getPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(commentService.getPage(page, size));
    }

    @ApiOperation("删除评论")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        commentService.delete(id);
        return Result.success();
    }
}
