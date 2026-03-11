package com.heritage.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductVO {
    private Long id;
    private String name;
    private Long productCategoryId;
    private String productCategoryName;
    private Long heritageItemId;
    private String heritageItemTitle;
    private Long inheritorId;
    private String inheritorName;
    private String inheritorAvatar;
    private String coverImage;
    private List<String> images;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private Integer sales;
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer status;
    private Integer isRecommend;
    private LocalDateTime createTime;
}
