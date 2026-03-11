package com.heritage.controller.admin;

import com.heritage.common.Result;
import com.heritage.service.StatisticsService;
import com.heritage.vo.CategoryDistributionVO;
import com.heritage.vo.HotContentVO;
import com.heritage.vo.StatisticsOverviewVO;
import com.heritage.vo.StatisticsTrendVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Api(tags = "管理员数据统计")
@RestController
@RequestMapping("/api/admin/statistics")
public class AdminStatisticsController {
    private final StatisticsService statisticsService;

    public AdminStatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @ApiOperation("统计总览")
    @GetMapping("/overview")
    public Result<StatisticsOverviewVO> getOverview() {
        return Result.success(statisticsService.getOverview());
    }

    @ApiOperation("非遗分类分布")
    @GetMapping("/heritage-category-distribution")
    public Result<List<CategoryDistributionVO>> getHeritageCategoryDistribution() {
        return Result.success(statisticsService.getHeritageCategoryDistribution());
    }

    @ApiOperation("热门内容排行")
    @GetMapping("/hot-content")
    public Result<List<HotContentVO>> getHotContent() {
        return Result.success(statisticsService.getHotContent());
    }

    @ApiOperation("趋势统计")
    @GetMapping("/trend")
    public Result<List<StatisticsTrendVO>> getTrend(@RequestParam(defaultValue = "users") String metric,
                                                    @RequestParam(defaultValue = "day") String granularity,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return Result.success(statisticsService.getTrend(metric, granularity, startDate, endDate));
    }
}
