package com.heritage.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.service.ProductService;
import com.heritage.vo.ProductVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "后台商品接口")
@RestController
@RequestMapping("/api/admin/product")
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {
    @Autowired
    private ProductService productService;

    @ApiOperation("商品分页列表")
    @GetMapping("/page")
    public Result<Page<ProductVO>> getProductPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {
        return Result.success(productService.getAdminProductList(page, size, status, categoryId, keyword));
    }

    @ApiOperation("商品详情")
    @GetMapping("/{id}")
    public Result<ProductVO> getProductDetail(@PathVariable Long id) {
        return Result.success(productService.getAdminProductDetail(id));
    }

    @ApiOperation("更新商品状态")
    @PutMapping("/{id}/status")
    public Result<?> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        productService.updateStatus(id, status);
        return Result.success();
    }

    @ApiOperation("更新商品推荐状态")
    @PutMapping("/{id}/recommend")
    public Result<?> updateRecommend(@PathVariable Long id, @RequestParam Integer isRecommend) {
        productService.updateRecommend(id, isRecommend);
        return Result.success();
    }

    @ApiOperation("删除商品")
    @DeleteMapping("/{id}")
    public Result<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success();
    }
}
