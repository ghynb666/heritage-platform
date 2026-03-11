package com.heritage.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDTO {
    private String nickname;
    private String avatar;
    private String email;
    private String phone;
    private Integer gender;
    private LocalDate birthday;
    private String province;
    private String city;
    private String area;
    private String address;
    private String signature;
    private Long heritageCategoryId;
}
