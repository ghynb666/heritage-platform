package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.service.UserService;
import com.heritage.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户接口")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("获取当前用户信息")
    @GetMapping("/info")
    public Result<UserVO> getUserInfo(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        UserVO user = userService.getUserById(userId);
        return Result.success(user);
    }

    @ApiOperation("获取当前用户角色")
    @GetMapping("/roles")
    public Result<List<String>> getUserRoles(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(userService.getUserRoles(userId));
    }
}
