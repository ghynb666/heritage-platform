package com.heritage.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("heritage_image")
public class HeritageImage {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long heritageItemId;

    private String imageUrl;

    private String description;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}
