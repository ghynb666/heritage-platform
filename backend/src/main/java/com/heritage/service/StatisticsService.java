package com.heritage.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.heritage.entity.AccessLog;
import com.heritage.entity.ForumPost;
import com.heritage.entity.HeritageCategory;
import com.heritage.entity.HeritageItem;
import com.heritage.entity.News;
import com.heritage.entity.Order;
import com.heritage.entity.Product;
import com.heritage.entity.SysUser;
import com.heritage.mapper.AccessLogMapper;
import com.heritage.mapper.ForumPostMapper;
import com.heritage.mapper.HeritageCategoryMapper;
import com.heritage.mapper.HeritageItemMapper;
import com.heritage.mapper.NewsMapper;
import com.heritage.mapper.OrderMapper;
import com.heritage.mapper.ProductMapper;
import com.heritage.mapper.UserMapper;
import com.heritage.mapper.UserRoleMapper;
import com.heritage.vo.CategoryDistributionVO;
import com.heritage.vo.HotContentVO;
import com.heritage.vo.StatisticsOverviewVO;
import com.heritage.vo.StatisticsTrendVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    private static final List<Integer> PAID_STATUSES = Arrays.asList(1, 2);

    private final UserMapper userMapper;
    private final UserRoleMapper userRoleMapper;
    private final HeritageItemMapper heritageItemMapper;
    private final HeritageCategoryMapper heritageCategoryMapper;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final AccessLogMapper accessLogMapper;
    private final NewsMapper newsMapper;
    private final ForumPostMapper forumPostMapper;

    public StatisticsService(UserMapper userMapper,
                             UserRoleMapper userRoleMapper,
                             HeritageItemMapper heritageItemMapper,
                             HeritageCategoryMapper heritageCategoryMapper,
                             ProductMapper productMapper,
                             OrderMapper orderMapper,
                             AccessLogMapper accessLogMapper,
                             NewsMapper newsMapper,
                             ForumPostMapper forumPostMapper) {
        this.userMapper = userMapper;
        this.userRoleMapper = userRoleMapper;
        this.heritageItemMapper = heritageItemMapper;
        this.heritageCategoryMapper = heritageCategoryMapper;
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.accessLogMapper = accessLogMapper;
        this.newsMapper = newsMapper;
        this.forumPostMapper = forumPostMapper;
    }

    public StatisticsOverviewVO getOverview() {
        StatisticsOverviewVO overview = new StatisticsOverviewVO();
        overview.setTotalUsers(userMapper.selectCount(null));
        overview.setNewUsersIn7Days(countUsersFrom(LocalDate.now().minusDays(6)));
        overview.setInheritorCount((long) userRoleMapper.selectUserIdsByRoleKey("INHERITOR").size());
        overview.setHeritageItemCount(heritageItemMapper.selectCount(null));
        overview.setProductCount(productMapper.selectCount(null));
        overview.setProductSales(sumProductSales());
        overview.setOrderCount(orderMapper.selectCount(null));
        overview.setTotalAmount(sumPaidAmount(null, null));
        overview.setTotalVisits(accessLogMapper.selectCount(null));
        return overview;
    }

    public List<CategoryDistributionVO> getHeritageCategoryDistribution() {
        List<HeritageCategory> categories = heritageCategoryMapper.selectList(new LambdaQueryWrapper<HeritageCategory>()
                .orderByAsc(HeritageCategory::getSort)
                .orderByAsc(HeritageCategory::getId));
        Map<Long, Long> itemCountMap = heritageItemMapper.selectList(null).stream()
                .collect(Collectors.groupingBy(HeritageItem::getHeritageCategoryId, Collectors.counting()));
        List<CategoryDistributionVO> result = new ArrayList<>();
        for (HeritageCategory category : categories) {
            CategoryDistributionVO vo = new CategoryDistributionVO();
            vo.setCategoryId(category.getId());
            vo.setCategoryName(category.getName());
            vo.setValue(itemCountMap.getOrDefault(category.getId(), 0L));
            result.add(vo);
        }
        return result;
    }

    public List<HotContentVO> getHotContent() {
        List<HotContentVO> merged = new ArrayList<>();
        merged.addAll(heritageItemMapper.selectList(new LambdaQueryWrapper<HeritageItem>()
                .orderByDesc(HeritageItem::getViewCount).last("LIMIT 5"))
                .stream().map(this::toHeritageHotContent).collect(Collectors.toList()));
        merged.addAll(newsMapper.selectList(new LambdaQueryWrapper<News>()
                .orderByDesc(News::getViewCount).last("LIMIT 5"))
                .stream().map(this::toNewsHotContent).collect(Collectors.toList()));
        merged.addAll(forumPostMapper.selectList(new LambdaQueryWrapper<ForumPost>()
                .orderByDesc(ForumPost::getViewCount).last("LIMIT 5"))
                .stream().map(this::toForumHotContent).collect(Collectors.toList()));
        merged.addAll(productMapper.selectList(new LambdaQueryWrapper<Product>()
                .orderByDesc(Product::getViewCount).last("LIMIT 5"))
                .stream().map(this::toProductHotContent).collect(Collectors.toList()));
        return merged.stream()
                .sorted(Comparator.comparing(HotContentVO::getViewCount, Comparator.nullsFirst(Comparator.reverseOrder())))
                .limit(10)
                .collect(Collectors.toList());
    }

    public List<StatisticsTrendVO> getTrend(String metric, String granularity, LocalDate startDate, LocalDate endDate) {
        String resolvedGranularity = resolveGranularity(granularity);
        LocalDate resolvedEnd = endDate == null ? LocalDate.now() : endDate;
        LocalDate resolvedStart = startDate == null ? defaultStart(resolvedGranularity, resolvedEnd) : startDate;
        Map<String, BigDecimal> buckets = initBuckets(resolvedGranularity, resolvedStart, resolvedEnd);

        if ("users".equals(metric)) {
            fillUserTrend(buckets, resolvedGranularity, resolvedStart, resolvedEnd);
        } else if ("orders".equals(metric)) {
            fillOrderTrend(buckets, resolvedGranularity, resolvedStart, resolvedEnd);
        } else if ("amount".equals(metric)) {
            fillAmountTrend(buckets, resolvedGranularity, resolvedStart, resolvedEnd);
        } else {
            fillVisitTrend(buckets, resolvedGranularity, resolvedStart, resolvedEnd);
        }

        List<StatisticsTrendVO> result = new ArrayList<>();
        buckets.forEach((label, value) -> {
            StatisticsTrendVO vo = new StatisticsTrendVO();
            vo.setLabel(label);
            vo.setValue(value);
            result.add(vo);
        });
        return result;
    }

    private long countUsersFrom(LocalDate startDate) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(SysUser::getCreateTime, startDate.atStartOfDay());
        return userMapper.selectCount(wrapper);
    }

    private long sumProductSales() {
        return productMapper.selectList(null).stream()
                .map(Product::getSales)
                .filter(value -> value != null && value > 0)
                .mapToLong(Integer::longValue)
                .sum();
    }

    private BigDecimal sumPaidAmount(LocalDateTime start, LocalDateTime end) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Order::getStatus, PAID_STATUSES);
        if (start != null) {
            wrapper.ge(Order::getPayTime, start);
        }
        if (end != null) {
            wrapper.lt(Order::getPayTime, end);
        }
        return orderMapper.selectList(wrapper).stream()
                .map(Order::getPayAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private HotContentVO toHeritageHotContent(HeritageItem item) {
        HotContentVO vo = new HotContentVO();
        vo.setType("非遗项目");
        vo.setId(item.getId());
        vo.setTitle(item.getTitle());
        vo.setViewCount(item.getViewCount() == null ? 0 : item.getViewCount());
        HeritageCategory category = item.getHeritageCategoryId() == null ? null : heritageCategoryMapper.selectById(item.getHeritageCategoryId());
        vo.setExtra(category == null ? "未分类" : category.getName());
        return vo;
    }

    private HotContentVO toNewsHotContent(News news) {
        HotContentVO vo = new HotContentVO();
        vo.setType("资讯");
        vo.setId(news.getId());
        vo.setTitle(news.getTitle());
        vo.setViewCount(news.getViewCount() == null ? 0 : news.getViewCount());
        vo.setExtra(StringUtils.hasText(news.getAuthorName()) ? news.getAuthorName() : "管理员");
        return vo;
    }

    private HotContentVO toForumHotContent(ForumPost post) {
        HotContentVO vo = new HotContentVO();
        vo.setType("论坛帖子");
        vo.setId(post.getId());
        vo.setTitle(post.getTitle());
        vo.setViewCount(post.getViewCount() == null ? 0 : post.getViewCount());
        vo.setExtra(StringUtils.hasText(post.getAuthorName()) ? post.getAuthorName() : "匿名用户");
        return vo;
    }

    private HotContentVO toProductHotContent(Product product) {
        HotContentVO vo = new HotContentVO();
        vo.setType("文创商品");
        vo.setId(product.getId());
        vo.setTitle(product.getName());
        vo.setViewCount(product.getViewCount() == null ? 0 : product.getViewCount());
        vo.setExtra("销量 " + (product.getSales() == null ? 0 : product.getSales()));
        return vo;
    }

    private void fillUserTrend(Map<String, BigDecimal> buckets, String granularity, LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(SysUser::getCreateTime, startDate.atStartOfDay())
                .lt(SysUser::getCreateTime, endDate.plusDays(1).atStartOfDay());
        userMapper.selectList(wrapper).forEach(user ->
                incrementBucket(buckets, granularity, user.getCreateTime().toLocalDate(), BigDecimal.ONE));
    }

    private void fillOrderTrend(Map<String, BigDecimal> buckets, String granularity, LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(Order::getCreateTime, startDate.atStartOfDay())
                .lt(Order::getCreateTime, endDate.plusDays(1).atStartOfDay());
        orderMapper.selectList(wrapper).forEach(order ->
                incrementBucket(buckets, granularity, order.getCreateTime().toLocalDate(), BigDecimal.ONE));
    }

    private void fillAmountTrend(Map<String, BigDecimal> buckets, String granularity, LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Order::getStatus, PAID_STATUSES)
                .isNotNull(Order::getPayTime)
                .ge(Order::getPayTime, startDate.atStartOfDay())
                .lt(Order::getPayTime, endDate.plusDays(1).atStartOfDay());
        orderMapper.selectList(wrapper).forEach(order -> {
            BigDecimal amount = order.getPayAmount() == null ? BigDecimal.ZERO : order.getPayAmount();
            incrementBucket(buckets, granularity, order.getPayTime().toLocalDate(), amount);
        });
    }

    private void fillVisitTrend(Map<String, BigDecimal> buckets, String granularity, LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<AccessLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(AccessLog::getCreateTime, startDate.atStartOfDay())
                .lt(AccessLog::getCreateTime, endDate.plusDays(1).atStartOfDay());
        accessLogMapper.selectList(wrapper).forEach(log ->
                incrementBucket(buckets, granularity, log.getCreateTime().toLocalDate(), BigDecimal.ONE));
    }

    private Map<String, BigDecimal> initBuckets(String granularity, LocalDate startDate, LocalDate endDate) {
        Map<String, BigDecimal> buckets = new LinkedHashMap<>();
        if ("week".equals(granularity)) {
            LocalDate cursor = startDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endCursor = endDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            while (!cursor.isAfter(endCursor)) {
                buckets.put(formatLabel(granularity, cursor), BigDecimal.ZERO);
                cursor = cursor.plusWeeks(1);
            }
            return buckets;
        }
        if ("month".equals(granularity)) {
            YearMonth cursor = YearMonth.from(startDate);
            YearMonth endCursor = YearMonth.from(endDate);
            while (!cursor.isAfter(endCursor)) {
                buckets.put(cursor.toString(), BigDecimal.ZERO);
                cursor = cursor.plusMonths(1);
            }
            return buckets;
        }
        LocalDate cursor = startDate;
        while (!cursor.isAfter(endDate)) {
            buckets.put(cursor.format(DateTimeFormatter.ISO_LOCAL_DATE), BigDecimal.ZERO);
            cursor = cursor.plusDays(1);
        }
        return buckets;
    }

    private void incrementBucket(Map<String, BigDecimal> buckets, String granularity, LocalDate date, BigDecimal delta) {
        String key = formatLabel(granularity, date);
        buckets.computeIfPresent(key, (ignored, current) -> current.add(delta));
    }

    private String formatLabel(String granularity, LocalDate date) {
        if ("week".equals(granularity)) {
            return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        if ("month".equals(granularity)) {
            return YearMonth.from(date).toString();
        }
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private String resolveGranularity(String granularity) {
        if ("week".equals(granularity) || "month".equals(granularity)) {
            return granularity;
        }
        return "day";
    }

    private LocalDate defaultStart(String granularity, LocalDate endDate) {
        if ("week".equals(granularity)) {
            return endDate.minusWeeks(11).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        }
        if ("month".equals(granularity)) {
            return endDate.minusMonths(11).withDayOfMonth(1);
        }
        return endDate.minusDays(6);
    }
}
