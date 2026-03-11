package com.heritage.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageVO {
    private Long id;
    private String content;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Long parentId;
    private Long replyUserId;
    private String replyUserName;
    private Integer status;
    private LocalDateTime createTime;
}
