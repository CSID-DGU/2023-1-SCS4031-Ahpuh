package com.example.ahpuh.admin.dto;

import com.example.ahpuh.cctv.entity.PositionEntity;
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
    private List<PostCctvPosReq> posList;
}
