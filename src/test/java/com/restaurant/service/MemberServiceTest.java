package com.restaurant.service;

import com.restaurant.controller.dto.MemberDto;
import com.restaurant.entity.Member;
import com.restaurant.entity.common.Address;
import com.restaurant.entity.type.MemberType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    public void joinMember() throws Exception {
        //given
        MemberDto member = setMember();

        //when
        String memberId = memberService.join(member);

        //then
        Member findMember = memberService.findById(memberId);

        assertThat(memberId).isEqualTo(findMember.getMemberId());
    }

    @Test
    public void updateMember() throws Exception {
        //given
        MemberDto member = setMember();

        //when
        String memberId = memberService.join(member);

        Member findMember = memberService.findById(memberId);
        findMember.setMemberName("사용자2");
        findMember.setPhoneNum("010-1111-2222");
        findMember.setMemberType(MemberType.OWNER);

        //then
        Member updateMemeber = memberService.findById(findMember.getMemberId());

        assertThat(findMember.getMemberName()).isEqualTo(updateMemeber.getMemberName());
    }

    @Test
    public void login() throws Exception {
        //given
        MemberDto member = setMember();
        String memberId = memberService.join(member);

        //when
        HttpServletRequest request = new MockHttpServletRequest();
        HttpSession session = memberService.login(request, member);

        //then
        Member findMember = memberService.findById(memberId);
        assertThat(session.getAttribute("MEMBER_INFO")).isEqualTo(findMember);
    }

    MemberDto setMember() {

        Address address = new Address("111", "222", "333");

        return MemberDto.builder()
                .memberName("사용자1")
                .phoneNum("010-1234-1234")
                .memberId("member1")
                .password("1234")
                .memberType(MemberType.CUSTOMER)
                .zipcode("111")
                .streetName("222")
                .detailAddress("333")
                .build();
    }
}