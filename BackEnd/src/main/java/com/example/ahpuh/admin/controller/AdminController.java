package com.example.ahpuh.admin.controller;

import com.example.ahpuh.admin.dto.PostAdminReq;
import com.example.ahpuh.admin.dto.PostAdminRes;
import com.example.ahpuh.admin.dto.PostCctvImgRes;
import com.example.ahpuh.admin.dto.PostLoginReq;
import com.example.ahpuh.admin.service.AdminService;
import com.example.ahpuh.admin.service.S3Service;
import com.example.ahpuh.jwt.dto.TokenDto;
import com.example.ahpuh.jwt.service.JwtService;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponse;
import com.example.ahpuh.util.BaseResponseStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@AllArgsConstructor
@Api(tags = {"수영장 관리자 관련 API"})
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private S3Service s3Service;

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

    @Operation(summary = "post cctv Img", description = "관리자 초기 설정 - cctv 이미지 업로드")
    @Parameter(name = "cctvImg", description = "업로드할 cctv 이미지", example = "cctv 이미지")
    @ResponseBody
    @PostMapping("/cctv/img")
    public BaseResponse<PostCctvImgRes> uploadImg(@RequestPart(value = "cctvImg") MultipartFile multipartFile) throws BaseException {
        String cctvImg = s3Service.uploadImg(multipartFile);
        PostCctvImgRes postCctvImgRes = adminService.uploadCctvImg(cctvImg);
        return new BaseResponse<>(postCctvImgRes);
    }


}
