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

    @Operation(summary = "post cctv Img & video", description = "관리자 초기 설정 - cctv 이미지, 비디오 업로드")
    @Parameter(name = "cctvFile", description = "업로드할 cctv 이미지와 동영상", example = "cctv 이미지, 동영상")
    @ResponseBody
    @PostMapping("/cctv/file")
    public BaseResponse<PostCctvFileRes> uploadFile(@RequestPart(value = "cctvFile") MultipartFile multipartFile) throws BaseException {
        String cctvImg = s3Service.uploadFile(multipartFile);
        PostCctvFileRes postCctvFileRes = adminService.uploadCctvFile(cctvImg);
        return new BaseResponse<>(postCctvFileRes);
    }

    @Operation(summary = "post cctv 레인 개수", description = "관리자 초기 설정 - cctv 레인 개수 전달")
    @Parameter(name = "cctvLine", description = "레인 개수", example = "4")
    @ResponseBody
    @PostMapping("/cctv/line")
    public BaseResponse<PostCctvLineRes> uploadLine(@RequestBody PostLineReq postLineReq) throws BaseException {
        PostCctvLineRes postCctvLineRes = adminService.uploadLine(postLineReq.getLineNum());
        return new BaseResponse<>(postCctvLineRes);
    }

    @Operation(summary = "post cctv 좌표", description = "관리자 초기 설정 - cctv 좌표값 전달")
    @Parameter(name = "cctvPos", description = "좌표값", example = "xPos = 21.0, yPos = 5.09")
    @ResponseBody
    @PostMapping("/cctv/pos")
    public BaseResponse<PostCctvPosRes> uploadPos(@RequestBody PostCctvPosListReq postCctvPosReq) throws BaseException {
        PostCctvPosRes posList = adminService.uploadPos(postCctvPosReq);
        return new BaseResponse<>(posList);
    }
}
