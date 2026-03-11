package com.heritage.dto;

import lombok.Data;

import java.util.List;

@Data
public class HeritageItemDTO {
    private Long id;
    private String title;
    private Long heritageCategoryId;
    private String coverImage;
    private String historyOrigin;
    private String craftFeature;
    private String content;
    private List<HeritageImageDTO> images;
    private List<HeritageVideoDTO> videos;
}
