package com.heritage.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private Integer gender;
    private LocalDateTime birthday;
    private String province;
    private String city;
    private String area;
    private String address;
    private String signature;
    private Long heritageCategoryId;
    private String heritageCategoryName;
    private LocalDateTime createTime;
}
