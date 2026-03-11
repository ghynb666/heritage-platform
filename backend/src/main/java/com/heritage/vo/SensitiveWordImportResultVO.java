package com.heritage.vo;

import lombok.Data;

import java.util.List;

@Data
public class SensitiveWordImportResultVO {
    private Integer totalCount;
    private Integer successCount;
    private Integer skippedCount;
    private List<String> duplicateSamples;
}
