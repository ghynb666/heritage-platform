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

import java.time.LocalDateTime;

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
        LocalDateTime now = LocalDateTime.now();
        wrapper.eq(Announcement::getStatus, status != null ? status : 1)
                .and(w -> w.isNull(Announcement::getStartTime).or().le(Announcement::getStartTime, now))
                .and(w -> w.isNull(Announcement::getEndTime).or().ge(Announcement::getEndTime, now));
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

