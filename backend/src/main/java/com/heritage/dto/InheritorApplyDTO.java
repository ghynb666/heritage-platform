package com.heritage.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class InheritorApplyDTO {
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    private String idCard;

    @NotBlank(message = "资质证明不能为空")
    private String certificateImages;

    private String description;

    @NotNull(message = "非遗类型不能为空")
    private Long heritageCategoryId;
}
