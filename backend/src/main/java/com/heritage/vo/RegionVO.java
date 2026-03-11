package com.heritage.vo;

import lombok.Data;

import java.util.List;

@Data
public class RegionVO {
    private Long id;
    private Long parentId;
    private String name;
    private String code;
    private Integer level;
    private Integer sort;
    private Integer status;
    private List<RegionVO> children;
}
