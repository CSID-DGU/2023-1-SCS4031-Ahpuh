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
public class PostVideoRes {
    @ApiModelProperty(example = "s3에 업로드된 url")
    private String s3url;
}
