package com.example.ahpuh.user.controller;

import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.cctv.dto.PostCctvFileRes;
import com.example.ahpuh.jwt.service.JwtService;
import com.example.ahpuh.user.dto.PostUserReq;
import com.example.ahpuh.user.service.UserService;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
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
    public BaseResponse<String> enterMember(@RequestBody PostUserReq postUserReq) throws BaseException {
        AdminEntity admin = jwtService.getAdmin();
        userService.enterMember(admin, postUserReq);
        return new BaseResponse<>("회원 등록을 완료하였습니다.");
    }
}
