package com.heritage.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.service.InheritorApplyService;
import com.heritage.vo.InheritorApplyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Api(tags = "传承人审核接口")
@RestController
@RequestMapping("/api/admin/inheritor")
@PreAuthorize("hasRole('ADMIN')")
public class InheritorAuditController {
    @Autowired
    private InheritorApplyService applyService;

    @ApiOperation("申请列表")
    @GetMapping("/apply/list")
    public Result<Page<InheritorApplyVO>> getApplyList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        return Result.success(applyService.getApplyList(page, size, status));
    }

    @ApiOperation("申请详情")
    @GetMapping("/apply/{id}")
    public Result<InheritorApplyVO> getApplyDetail(@PathVariable Long id) {
        return Result.success(applyService.getApplyDetail(id));
    }

    @ApiOperation("审核通过")
    @PostMapping("/apply/{id}/approve")
    public Result<?> approve(@PathVariable Long id, Authentication authentication) {
        Long reviewerId = (Long) authentication.getPrincipal();
        applyService.approve(id, reviewerId);
        return Result.success("审核通过");
    }

    @ApiOperation("审核拒绝")
    @PostMapping("/apply/{id}/reject")
    public Result<?> reject(@PathVariable Long id, @RequestParam String reason, Authentication authentication) {
        Long reviewerId = (Long) authentication.getPrincipal();
        applyService.reject(id, reviewerId, reason);
        return Result.success("已拒绝");
    }
}
