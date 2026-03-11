package com.heritage.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("heritage_video")
public class HeritageVideo {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long heritageItemId;

    private String videoUrl;

    private String coverUrl;

    private String title;

    private String description;

    private Integer duration;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}
