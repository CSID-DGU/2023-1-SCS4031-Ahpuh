package com.example.ahpuh.cctv.controller;

import com.example.ahpuh.admin.service.S3Service;
import com.example.ahpuh.cctv.dto.*;
import com.example.ahpuh.cctv.service.CctvService;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponse;
import io.swagger.annotations.Api;
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
@RequestMapping(value = "cctv")
@Api(tags = {"CCTV 설정 API"})
public class CctvController {

    @Autowired
    private CctvService cctvService;
    @Autowired
    private S3Service s3Service;

    @Operation(summary = "post cctv Img & video", description = "관리자 초기 설정 - cctv 이미지, 비디오 업로드")
    @Parameter(name = "cctvFile", description = "업로드할 cctv 이미지와 동영상", example = "cctv 이미지, 동영상")
    @ResponseBody
    @PostMapping("/cctv/file")
    public BaseResponse<PostCctvFileRes> uploadFile(@RequestPart(value = "cctvFile") MultipartFile multipartFile) throws BaseException {
        String cctvImg = s3Service.uploadFile(multipartFile);
        PostCctvFileRes postCctvFileRes = cctvService.uploadCctvFile(cctvImg);
        return new BaseResponse<>(postCctvFileRes);
    }

    @Operation(summary = "post cctv 레인 개수", description = "관리자 초기 설정 - cctv 레인 개수 전달")
    @Parameter(name = "cctvLine", description = "레인 개수", example = "4")
    @ResponseBody
    @PostMapping("/cctv/line")
    public BaseResponse<PostCctvLineRes> uploadLine(@RequestBody PostLineReq postLineReq) throws BaseException {
        PostCctvLineRes postCctvLineRes = cctvService.uploadLine(postLineReq.getLineNum());
        return new BaseResponse<>(postCctvLineRes);
    }

    @Operation(summary = "post cctv 좌표", description = "관리자 초기 설정 - cctv 좌표값 전달")
    @Parameter(name = "cctvPos", description = "좌표값", example = "xPos = 21.0, yPos = 5.09")
    @ResponseBody
    @PostMapping("/cctv/pos")
    public BaseResponse<PostCctvPosRes> uploadPos(@RequestBody PostCctvPosListReq postCctvPosReq) throws BaseException {
        PostCctvPosRes posList = cctvService.uploadPos(postCctvPosReq);
        return new BaseResponse<>(posList);
    }
}
