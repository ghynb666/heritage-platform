package com.heritage.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ForumPostVO {
    private Long id;
    private String title;
    private String content;
    private List<String> images;
    private Long authorId;
    private Integer authorType;
    private String authorName;
    private String authorAvatar;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Integer status;
    private Integer isTop;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
