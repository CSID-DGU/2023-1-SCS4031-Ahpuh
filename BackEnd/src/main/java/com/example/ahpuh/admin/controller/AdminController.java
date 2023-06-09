package com.example.ahpuh.admin.controller;

import com.example.ahpuh.admin.dto.*;
import com.example.ahpuh.admin.service.AdminService;
import com.example.ahpuh.admin.service.S3Service;
import com.example.ahpuh.jwt.service.JwtService;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponse;
import com.example.ahpuh.util.BaseResponseStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@AllArgsConstructor
@Api(tags = {"수영장 관리자 관련 API"})
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ResponseBody
    @ApiOperation(value = "회원가입", notes ="비밀번호 validation 규칙은 8글자 이상 16글자 이하/문자 + 숫자 섞어서")
    @PostMapping("/sign-up")
    public BaseResponse<String> signUp(@RequestBody PostAdminReq admin) {
        try {
            adminService.signUp(admin);
            return new BaseResponse<>("회원가입에 성공하였습니다.");

        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ResponseBody
    @ApiOperation(value = "로그인", notes ="이메일, 비밀번호 입력")
    @PostMapping("/sign-in")
    public BaseResponse<PostAdminRes> signIn(@RequestBody PostLoginReq admin) {
        if (admin.getEmail().length() == 0 || admin.getEmail() == null) {
            return new BaseResponse<>(BaseResponseStatus.POST_ADMIN_EMPTY_EMAIL);
        }
        try {
            PostAdminRes token = adminService.signIn(admin);
            return new BaseResponse<>(token);
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }


}
