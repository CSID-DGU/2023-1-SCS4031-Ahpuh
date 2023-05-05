package com.example.ahpuh.admin.dto;

import com.example.ahpuh.util.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostAdminReq {
    private String email;
    private String pwd;
    private String poolName;
    private String poolNum;
    private String poolAddress;
    @ApiModelProperty(hidden = true)
    private Role role;
}
