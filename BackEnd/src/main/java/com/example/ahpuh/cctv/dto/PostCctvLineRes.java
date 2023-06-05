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
public class PostCctvLineRes {
    @ApiModelProperty(example = "라인 개수")
    private Long lineNum;
}
