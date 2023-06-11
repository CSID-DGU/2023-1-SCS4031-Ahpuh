package com.example.ahpuh.cctv.service;

import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.admin.repository.AdminRepository;
import com.example.ahpuh.cctv.dto.*;
import com.example.ahpuh.cctv.entity.CctvEntity;
import com.example.ahpuh.cctv.entity.PositionEntity;
import com.example.ahpuh.cctv.repository.CctvRepository;
import com.example.ahpuh.cctv.repository.PositionRepository;
import com.example.ahpuh.jwt.service.JwtService;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponseStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CctvService {
    private final AdminRepository adminRepository;
    private final CctvRepository cctvRepository;
    private final JwtService jwtService;
    private final PositionRepository positionRepository;
    @Value("${flask.flask-url}")
    private String FLASK_URL;

    public PostCctvFileRes uploadCctvFile(AdminEntity admin, String cctvImg) throws BaseException {
        createCCTV(admin, cctvImg);
        return new PostCctvFileRes(cctvImg);
    }

    public void createCCTV(AdminEntity admin, String cctvImg) {
        CctvEntity cctv = CctvEntity.builder()
                .cctvImage(cctvImg)
                .cctvFile("cctv file")
                .lineNum(0L)
                .adminIdx(admin)
                .build();
        cctvRepository.save(cctv);
    }

    public PostCctvLineRes uploadLine(AdminEntity admin, Long lineNum) throws BaseException {
        // cctvIdx 조회 후 저장
        CctvEntity cctv = cctvRepository.findByAdminIdx(admin)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_CCTV));
        cctv.uploadLine(lineNum);
        cctvRepository.save(cctv);
        return new PostCctvLineRes(lineNum);
    }

    public PostCctvPosRes uploadPos(AdminEntity admin, PostCctvPosListReq posReq) throws BaseException {
        CctvEntity cctv = cctvRepository.findByAdminIdx(admin)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_CCTV));
        PositionEntity pos = positionRepository.findByCctvIdx(cctv)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.EXIST_POSITION));
        List<PostCctvPosReq> posList = cctvSave(cctv, posReq);
        return new PostCctvPosRes(posList);
    }

    public List<PostCctvPosReq> cctvSave(CctvEntity cctv, PostCctvPosListReq posReq) throws BaseException {
        List<PostCctvPosReq> posList = new ArrayList<>();
        if(posReq.getPosList().size() != cctv.getLineNum()){
            throw new BaseException(BaseResponseStatus.INVALID_POS_NUM);
        }
        for(PostCctvPosReq i : posReq.getPosList()) {
            PositionEntity pos = PositionEntity.builder()
                    .cctvIdx(cctv)
                    .xPos(i.getX())
                    .yPos(i.getY())
                    .build();
            positionRepository.save(pos);
            posList.add(new PostCctvPosReq(pos.getXPos(), pos.getYPos()));
        }
        return posList;
    }

    // 딥러닝 서버 연결 테스트용
    public PostVideoRes uploadVideo(AdminEntity admin, MultipartFile video) throws BaseException, JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("video", video.getResource());

        HttpEntity<MultiValueMap<String, Object>> req = new HttpEntity<>(body, httpHeaders);
        HttpEntity<String> s3url = restTemplate.postForEntity(FLASK_URL, req, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        PostVideoRes postVideoRes = objectMapper.readValue(s3url.getBody(), PostVideoRes.class);

        // cctv url DB 저장
        CctvEntity cctv = cctvRepository.findByAdminIdx(admin)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_CCTV));
        cctv.uploadVideo(postVideoRes.getS3url());
        cctvRepository.save(cctv);

        return postVideoRes;
    }
}
