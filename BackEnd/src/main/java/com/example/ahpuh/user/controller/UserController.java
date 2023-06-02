package com.example.ahpuh.user.controller;

import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.jwt.service.JwtService;
import com.example.ahpuh.user.dto.PatchLectureReq;
import com.example.ahpuh.user.dto.PatchUserReq;
import com.example.ahpuh.user.dto.PostUserReq;
import com.example.ahpuh.user.dto.PostUserRes;
import com.example.ahpuh.user.service.UserService;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "user")
@Api(tags = {"수영장 회원 관련 API"})
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @Operation(summary = "post user info", description = "수영장 회원 추가")
    @Parameter(name = "postUserReq", description = "회원 등록 정보", example = "이름, 전화번호, 주소, 성별 등")
    @ResponseBody
    @PostMapping("/enterMember")
    public BaseResponse<PostUserRes> enterMember(@RequestBody PostUserReq postUserReq) throws BaseException {
        AdminEntity admin = jwtService.getAdmin();
        PostUserRes res = userService.enterMember(admin, postUserReq);
        return new BaseResponse<>(res);
    }

    @Operation(summary = "patch user info", description = "수영장 회원 정보 수정")
    @Parameter(name = "patchUserReq", description = "회원 정보 수정 사항", example = "이름, 전화번호, 주소, 성별 등")
    @ResponseBody
    @PatchMapping("/modifyMember/{userIdx}")
    public BaseResponse<String> modifyMember(@RequestBody PatchUserReq patchUserReq, @PathVariable("userIdx") Long userIdx) throws BaseException {
        AdminEntity admin = jwtService.getAdmin();
        userService.modifyMember(userIdx, patchUserReq);
        return new BaseResponse<>("회원 정보 수정을 완료하였습니다.");
    }

    @Operation(summary = "patch user info", description = "회원 수강 상태 수정")
    @Parameter(name = "patchLectureReq", description = "회원 수강 상태", example = "INACTIVE")
    @ResponseBody
    @PatchMapping("/modifyLecStatus/{userIdx}")
    public BaseResponse<String> modifyMemberStatus(@RequestBody PatchLectureReq patchLectureReq, @PathVariable("userIdx") Long userIdx) throws BaseException {
        AdminEntity admin = jwtService.getAdmin();
        userService.modifyMemberStatus(userIdx, patchLectureReq);
        return new BaseResponse<>("수강 상태를 변경하였습니다.");
    }
}
