package com.heritage.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentVO {
    private Long id;
    private String content;
    private Long userId;
    private String userName;
    private String userAvatar;
    private Integer targetType;
    private String targetTypeName;
    private Long targetId;
    private Long parentId;
    private Long replyUserId;
    private String replyUserName;
    private Integer likeCount;
    private Integer status;
    private LocalDateTime createTime;
}
