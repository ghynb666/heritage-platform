package com.heritage.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heritage.common.Result;
import com.heritage.entity.Announcement;
import com.heritage.mapper.AnnouncementMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "鍏憡鎺ュ彛")
@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementMapper announcementMapper;

    @ApiOperation("鍏憡鍒楄〃")
    @GetMapping("/list")
    public Result<Page<Announcement>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status) {
        Page<Announcement> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Announcement::getStatus, status);
        }
        wrapper.orderByAsc(Announcement::getSort)
                .orderByDesc(Announcement::getCreateTime);
        return Result.success(announcementMapper.selectPage(pageParam, wrapper));
    }

    @ApiOperation("鍏憡璇︽儏")
    @GetMapping("/{id}")
    public Result<Announcement> detail(@PathVariable Long id) {
        return Result.success(announcementMapper.selectById(id));
    }
}

