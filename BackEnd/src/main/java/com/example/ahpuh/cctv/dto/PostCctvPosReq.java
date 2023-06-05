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
public class PostCctvPosReq {
    @ApiModelProperty(example = "x값")
    private String x;
    @ApiModelProperty(example = "y값")
    private String y;
}

