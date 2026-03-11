package com.heritage.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("heritage_item")
public class HeritageItem {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private Long heritageCategoryId;

    private String coverImage;

    private String historyOrigin;

    private String craftFeature;

    private String content;

    private Long publisherId;

    private Integer publisherType;

    private Integer viewCount;

    private Integer likeCount;

    private Integer favoriteCount;

    private Integer commentCount;

    private Integer status;

    private Integer isRecommend;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
