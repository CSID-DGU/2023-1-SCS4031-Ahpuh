package com.example.ahpuh.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserReq {
    @ApiModelProperty(example = "이름")
    private String name;
    @ApiModelProperty(example = "전화번호")
    private String phoneNum;
    @ApiModelProperty(example = "나이")
    private String age;
    @ApiModelProperty(example = "성별")
    private String gender;
    @ApiModelProperty(example = "생년월일")
    private String birth;
    @ApiModelProperty(example = "보호자 연락처")
    private String parentalContacts;
}
