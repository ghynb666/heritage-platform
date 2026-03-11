package com.heritage.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HeritageImageVO {
    private Long id;
    private String imageUrl;
    private String description;
    private Integer sort;
    private LocalDateTime createTime;
}
