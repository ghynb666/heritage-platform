package com.heritage.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MessageReplyDTO {
    @NotBlank(message = "回复内容不能为空")
    private String content;
}
