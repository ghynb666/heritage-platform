package com.heritage.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HeritageVideoVO {
    private Long id;
    private String videoUrl;
    private String coverUrl;
    private String title;
    private String description;
    private Integer duration;
    private Integer sort;
    private LocalDateTime createTime;
}
