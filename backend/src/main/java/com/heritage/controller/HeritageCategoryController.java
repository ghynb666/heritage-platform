package com.heritage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.entity.HeritageCategory;
import com.heritage.service.HeritageCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "非遗分类接口")
@RestController
@RequestMapping("/api/heritage")
public class HeritageCategoryController {
    @Autowired
    private HeritageCategoryService categoryService;

    @ApiOperation("分类列表")
    @GetMapping("/category/list")
    public Result<List<HeritageCategory>> getCategoryList() {
        return Result.success(categoryService.getAllCategories());
    }

    @ApiOperation("分类分页列表(管理)")
    @GetMapping("/category/page")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Page<HeritageCategory>> getCategoryPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(categoryService.getCategoryPage(page, size));
    }

    @ApiOperation("新增分类")
    @PostMapping("/category")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> addCategory(@RequestBody HeritageCategory category) {
        categoryService.save(category);
        return Result.success();
    }

    @ApiOperation("更新分类")
    @PutMapping("/category")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> updateCategory(@RequestBody HeritageCategory category) {
        categoryService.update(category);
        return Result.success();
    }

    @ApiOperation("删除分类")
    @DeleteMapping("/category/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success();
    }

    @ApiOperation("批量更新排序")
    @PutMapping("/category/sort")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> updateSort(@RequestBody List<HeritageCategory> categories) {
        categoryService.updateSort(categories);
        return Result.success();
    }
}
