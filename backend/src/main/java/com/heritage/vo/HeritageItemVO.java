package com.heritage.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HeritageItemVO {
    private Long id;
    private String title;
    private Long heritageCategoryId;
    private String heritageCategoryName;
    private String coverImage;
    private String historyOrigin;
    private String craftFeature;
    private String content;
    private Long publisherId;
    private Integer publisherType;
    private String publisherName;
    private String publisherAvatar;
    private Integer viewCount;
    private Integer likeCount;
    private Integer favoriteCount;
    private Integer commentCount;
    private Integer status;
    private Integer isRecommend;
    private LocalDateTime createTime;
    private List<HeritageImageVO> images;
    private List<HeritageVideoVO> videos;
}
