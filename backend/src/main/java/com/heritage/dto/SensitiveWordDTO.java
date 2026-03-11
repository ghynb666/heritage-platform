package com.heritage.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SensitiveWordDTO {
    @NotBlank(message = "敏感词不能为空")
    private String word;

    @NotNull(message = "状态不能为空")
    private Integer status;

    private String remark;
}
