package com.example.ahpuh.admin.controller;

import com.example.ahpuh.admin.dto.PostAdminReq;
import com.example.ahpuh.admin.service.AdminService;
import com.example.ahpuh.jwt.dto.TokenDto;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@AllArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ResponseBody
    @PostMapping("/sign-up")
    public BaseResponse<TokenDto> signUp(@RequestBody PostAdminReq admin) {
        try {
            TokenDto token = adminService.signUp(admin);
            return new BaseResponse<>(token);

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
