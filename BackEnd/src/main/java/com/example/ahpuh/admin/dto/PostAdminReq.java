package com.example.ahpuh.admin.dto;

import com.example.ahpuh.util.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostAdminReq {
    @ApiModelProperty(example = "이메일")
    private String email;
    @ApiModelProperty(example = "비밀번호")
    private String pwd;
    @ApiModelProperty(example = "수영장 이름")
    private String poolName;
    @ApiModelProperty(example = "수영장 연락처")
    private String poolNum;
    @ApiModelProperty(example = "수영장 주소")
    private String poolAddress;
    @ApiModelProperty(hidden = true)
    private Role role;
}
