package com.heritage.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InheritorApplyVO {
    private Long id;
    private Long userId;
    private String username;
    private String userAvatar;
    private Long heritageCategoryId;
    private String heritageCategoryName;
    private String realName;
    private String idCard;
    private String certificateImages;
    private String description;
    private Integer status;
    private String rejectReason;
    private LocalDateTime createTime;
    private LocalDateTime reviewTime;
}
