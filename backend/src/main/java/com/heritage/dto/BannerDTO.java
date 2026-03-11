package com.heritage.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class BannerDTO {
    private Long id;
    private String title;

    @NotBlank(message = "图片地址不能为空")
    private String imageUrl;

    private String linkUrl;
    private Integer linkType;
    private Long linkId;
    private Integer sort;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
