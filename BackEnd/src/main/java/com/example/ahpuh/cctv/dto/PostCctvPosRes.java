package com.example.ahpuh.cctv.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCctvPosRes {
    @ApiModelProperty(example = "좌표 리스트")
    private List<PostCctvPosReq> posList;
}
