package com.heritage.controller;

import com.heritage.common.Result;
import com.heritage.entity.HeritageCategory;
import com.heritage.mapper.HeritageCategoryMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "非遗分类接口")
@RestController
@RequestMapping("/api/heritage")
public class HeritageCategoryController {
    @Autowired
    private HeritageCategoryMapper categoryMapper;

    @ApiOperation("分类列表")
    @GetMapping("/category/list")
    public Result<List<HeritageCategory>> getCategoryList() {
        List<HeritageCategory> categories = categoryMapper.selectList(null);
        return Result.success(categories);
    }
}
