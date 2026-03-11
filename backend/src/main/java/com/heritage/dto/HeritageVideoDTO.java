package com.heritage.dto;

import lombok.Data;

@Data
public class HeritageVideoDTO {
    private String videoUrl;
    private String coverUrl;
    private String title;
    private String description;
    private Integer duration;
    private Integer sort;
}
