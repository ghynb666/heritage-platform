package com.heritage.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.dto.SensitiveWordDTO;
import com.heritage.service.SensitiveWordService;
import com.heritage.vo.SensitiveWordImportResultVO;
import com.heritage.vo.SensitiveWordVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "管理员敏感词管理")
@RestController
@RequestMapping("/api/admin/sensitive-word")
public class AdminSensitiveWordController {
    private final SensitiveWordService sensitiveWordService;

    public AdminSensitiveWordController(SensitiveWordService sensitiveWordService) {
        this.sensitiveWordService = sensitiveWordService;
    }

    @ApiOperation("敏感词分页列表")
    @GetMapping("/page")
    public Result<Page<SensitiveWordVO>> getPage(@RequestParam(defaultValue = "1") Integer page,
                                                 @RequestParam(defaultValue = "10") Integer size,
                                                 @RequestParam(required = false) String keyword,
                                                 @RequestParam(required = false) Integer status) {
        return Result.success(sensitiveWordService.getPage(page, size, keyword, status));
    }

    @ApiOperation("新增敏感词")
    @PostMapping
    public Result<?> add(@Validated @RequestBody SensitiveWordDTO dto) {
        sensitiveWordService.add(dto);
        return Result.success();
    }

    @ApiOperation("更新敏感词")
    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @Validated @RequestBody SensitiveWordDTO dto) {
        sensitiveWordService.update(id, dto);
        return Result.success();
    }

    @ApiOperation("删除敏感词")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        sensitiveWordService.delete(id);
        return Result.success();
    }

    @ApiOperation("导入敏感词")
    @PostMapping("/import")
    public Result<SensitiveWordImportResultVO> importWords(@RequestParam("file") MultipartFile file) {
        return Result.success(sensitiveWordService.importWords(file));
    }
}
