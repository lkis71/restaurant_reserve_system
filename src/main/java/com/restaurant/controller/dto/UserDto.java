package com.restaurant.controller.dto;

import com.restaurant.entity.common.Address;

import lombok.Getter;

@Getter
public class UserDto {
    
    private String hmpgId;
    private String password;
    private String userNm;
    private String phoneNum;
    private Address address;
    private String registNum;
    private String userType;
}