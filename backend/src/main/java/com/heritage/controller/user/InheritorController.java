package com.heritage.controller.user;

import com.heritage.common.Result;
import com.heritage.dto.InheritorApplyDTO;
import com.heritage.service.InheritorApplyService;
import com.heritage.vo.InheritorApplyVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "传承人申请接口")
@RestController
@RequestMapping("/api/user/inheritor")
public class InheritorController {
    @Autowired
    private InheritorApplyService applyService;

    @ApiOperation("提交传承人申请")
    @PostMapping("/apply")
    public Result<?> apply(Authentication authentication, @Validated @RequestBody InheritorApplyDTO dto) {
        Long userId = (Long) authentication.getPrincipal();
        applyService.apply(userId, dto);
        return Result.success("申请提交成功");
    }

    @ApiOperation("查询申请状态")
    @GetMapping("/apply/status")
    public Result<InheritorApplyVO> getApplyStatus(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(applyService.getApplyStatus(userId));
    }
}
