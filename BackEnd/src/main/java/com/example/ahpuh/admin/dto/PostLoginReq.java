package com.example.ahpuh.admin.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostLoginReq {
    private String email;
    private String pwd;
}
