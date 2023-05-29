package com.example.umc4_heron_template_jpa.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostMemberReq {
    private String email;
    private String nickName;
    private String password;
}
