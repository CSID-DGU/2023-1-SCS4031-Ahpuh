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
public class PostUserRes {
    @ApiModelProperty(example = "회원 인덱스")
    private Long userIdx;
    @ApiModelProperty(example = "응답 메세지")
    private String message;
}
