package com.example.ahpuh.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchUserReq {
    private String name;
    private String phoneNum;
    private String gender;
    private String age;
    private String address;
}
