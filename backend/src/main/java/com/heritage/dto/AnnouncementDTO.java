package com.heritage.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class AnnouncementDTO {
    private Long id;

    @NotBlank(message = "公告标题不能为空")
    private String title;

    private String content;
    private Integer type;
    private Integer status;
    private Integer sort;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
