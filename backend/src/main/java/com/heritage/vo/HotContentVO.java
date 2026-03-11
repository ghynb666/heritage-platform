package com.heritage.vo;

import lombok.Data;

@Data
public class HotContentVO {
    private String type;
    private Long id;
    private String title;
    private Integer viewCount;
    private String extra;
}
