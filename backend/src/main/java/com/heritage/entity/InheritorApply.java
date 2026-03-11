package com.heritage.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("inheritor_apply")
public class InheritorApply {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long heritageCategoryId;

    private String realName;

    private String idCard;

    private String certificateImages;

    private String description;

    private Integer status;

    private String rejectReason;

    private Long reviewerId;

    private LocalDateTime reviewTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
