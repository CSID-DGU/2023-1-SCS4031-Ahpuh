package com.example.ahpuh.cctv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCctvFileRes {
    @ApiModelProperty(example = "cctv 이미지")
    private String cctvImg;
}
