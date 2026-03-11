package com.heritage.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductCategoryVO {
    private Long id;
    private String name;
    private String icon;
    private String description;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
}
