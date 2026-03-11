package com.heritage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.entity.ProductCategory;
import com.heritage.service.ProductCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "后台商品分类接口")
@RestController
@RequestMapping("/api/admin/product/category")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @ApiOperation("商品分类列表")
    @GetMapping("/list")
    public Result<List<ProductCategory>> getCategoryList() {
        return Result.success(productCategoryService.getAllCategories());
    }

    @ApiOperation("商品分类分页")
    @GetMapping("/page")
    public Result<Page<ProductCategory>> getCategoryPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(productCategoryService.getCategoryPage(page, size));
    }

    @ApiOperation("新增商品分类")
    @PostMapping
    public Result<?> addCategory(@RequestBody ProductCategory category) {
        productCategoryService.save(category);
        return Result.success();
    }

    @ApiOperation("更新商品分类")
    @PutMapping
    public Result<?> updateCategory(@RequestBody ProductCategory category) {
        productCategoryService.update(category);
        return Result.success();
    }

    @ApiOperation("删除商品分类")
    @DeleteMapping("/{id}")
    public Result<?> deleteCategory(@PathVariable Long id) {
        productCategoryService.delete(id);
        return Result.success();
    }

    @ApiOperation("批量更新商品分类排序")
    @PutMapping("/sort")
    public Result<?> updateSort(@RequestBody List<ProductCategory> categories) {
        productCategoryService.updateSort(categories);
        return Result.success();
    }
}
