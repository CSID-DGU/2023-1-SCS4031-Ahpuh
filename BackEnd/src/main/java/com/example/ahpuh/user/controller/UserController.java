package com.example.ahpuh.user.controller;

import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.jwt.service.JwtService;
import com.example.ahpuh.user.dto.*;
import com.example.ahpuh.user.service.UserService;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "user")
@Api(tags = {"수영장 회원 관련 API"})
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;

    @Operation(summary = "post user info", description = "수영장 회원 추가")
    @Parameter(name = "postUserReq", description = "회원 등록 정보", example = "이름, 전화번호, 주소, 성별 등")
    @ApiOperation(value = "회원 등록", notes ="이름, 전화번호, 나이, 성별, 생년월일, 보호자 전화번호 && jwt 토큰 담기")
    @ResponseBody
    @PostMapping("/enterMember")
    public BaseResponse<PostUserRes> enterMember(@RequestBody PostUserReq postUserReq) throws BaseException {
        AdminEntity admin = jwtService.getAdmin();
        PostUserRes res = userService.enterMember(admin, postUserReq);
        return new BaseResponse<>(res);
    }

    @Operation(summary = "patch user info", description = "수영장 회원 정보 수정")
    @Parameter(name = "patchUserReq", description = "회원 정보 수정 사항", example = "이름, 전화번호, 주소, 성별 등")
    @ApiOperation(value = "회원 정보 수정", notes ="이름, 전화번호, 나이, 성별, 생년월일, 보호자 전화번호 수정&& jwt 토큰 담기")
    @ResponseBody
    @PatchMapping("/modifyMember/{userIdx}")
    public BaseResponse<String> modifyMember(@RequestBody PatchUserReq patchUserReq, @PathVariable("userIdx") Long userIdx) throws BaseException {
        AdminEntity admin = jwtService.getAdmin();
        userService.modifyMember(userIdx, patchUserReq);
        return new BaseResponse<>("회원 정보 수정을 완료하였습니다.");
    }

    @Operation(summary = "delete user info", description = "회원 삭제")
    @ApiOperation(value = "회원 삭제", notes ="이름, 전화번호, 보호자 전화번호 삭제")
    @ResponseBody
    @DeleteMapping("/{userIdx}")
    public BaseResponse<String> deleteMember(@PathVariable("userIdx") Long userIdx) throws BaseException {
        AdminEntity admin = jwtService.getAdmin();
        userService.deleteMember(userIdx);
        return new BaseResponse<>("회원 삭제를 완료하였습니다.");
    }

    @Operation(summary = "get member List", description = "회원 정보 리스트 조회")
    @ApiOperation(value = "회원 정보 리스트 조회", notes ="이름, 전화번호, 나이, 성별, 생년월일, 보호자 전화번호 조회 && jwt 토큰 담기")
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetUserRes>> getMemberList() throws BaseException {
        AdminEntity admin = jwtService.getAdmin();
        List<GetUserRes> userList = userService.getMemberList(admin);
        return new BaseResponse<>(userList);
    }

    @Operation(summary = "get member", description = "회원 정보 조회")
    @ApiOperation(value = "회원 정보 조회", notes ="회원별로 이름, 전화번호, 나이, 성별, 생년월일, 보호자 전화번호 조회 && jwt 토큰 담기")
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetUserRes> getMember(@PathVariable("userIdx") Long userIdx) throws BaseException {
        AdminEntity admin = jwtService.getAdmin();
        GetUserRes user = userService.getMember(userIdx);
        return new BaseResponse<>(user);
    }
}
