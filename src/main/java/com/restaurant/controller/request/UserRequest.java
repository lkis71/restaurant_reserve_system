package com.restaurant.controller.request;

import com.restaurant.entity.MemberType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequest {

    private Long id;
    private String memberId;
    private String password;
    private String memberName;
    private String phoneNum;
    private String zipcode;
    private String streetNm;
    private String detailAddress;
    private String registNum;
    private MemberType memberType;

    @Builder
    public UserRequest(Long id, String memberId, String password, String memberName, String phoneNum, String zipcode, String streetNm, String detailAddress, String registNum, MemberType memberType) {
        this.id = id;
        this.memberId = memberId;
        this.password = password;
        this.memberName = memberName;
        this.phoneNum = phoneNum;
        this.zipcode = zipcode;
        this.streetNm = streetNm;
        this.detailAddress = detailAddress;
        this.registNum = registNum;
        this.memberType = memberType;
    }
}
