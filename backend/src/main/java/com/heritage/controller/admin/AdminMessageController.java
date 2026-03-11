package com.heritage.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.dto.MessageReplyDTO;
import com.heritage.service.MessageService;
import com.heritage.vo.MessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "管理员留言接口")
@RestController
@RequestMapping("/api/admin/message")
@PreAuthorize("hasRole('ADMIN')")
public class AdminMessageController {
    @Autowired
    private MessageService messageService;

    @ApiOperation("留言分页列表")
    @GetMapping("/page")
    public Result<Page<MessageVO>> getPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(messageService.getPage(page, size));
    }

    @ApiOperation("删除留言")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        messageService.delete(id);
        return Result.success();
    }

    @ApiOperation("回复留言")
    @PostMapping("/{id}/reply")
    public Result<?> reply(@PathVariable Long id,
                           @Validated @RequestBody MessageReplyDTO dto,
                           Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        messageService.reply(id, dto, userId);
        return Result.success();
    }
}
