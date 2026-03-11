package com.heritage.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StatisticsOverviewVO {
    private Long totalUsers;
    private Long newUsersIn7Days;
    private Long inheritorCount;
    private Long heritageItemCount;
    private Long productCount;
    private Long productSales;
    private Long orderCount;
    private BigDecimal totalAmount;
    private Long totalVisits;
}
