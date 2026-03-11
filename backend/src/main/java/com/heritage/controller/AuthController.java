package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.dto.LoginDTO;
import com.heritage.dto.RegisterDTO;
import com.heritage.service.AuthService;
import com.heritage.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "认证接口")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO dto) {
        return Result.success(authService.login(dto));
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public Result<LoginVO> register(@Validated @RequestBody RegisterDTO dto) {
        return Result.success(authService.register(dto));
    }
}
