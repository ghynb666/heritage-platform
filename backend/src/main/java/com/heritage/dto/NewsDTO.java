package com.heritage.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewsDTO {
    private Long id;

    @NotBlank(message = "资讯标题不能为空")
    private String title;

    private String coverImage;
    private String summary;
    private String content;
    private Integer status;
}
