package com.heritage.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SensitiveWordVO {
    private Long id;
    private String word;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
