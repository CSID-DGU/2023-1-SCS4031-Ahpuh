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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CctvService {
    private final AdminRepository adminRepository;
    private final CctvRepository cctvRepository;
    private final JwtService jwtService;
    private final PositionRepository positionRepository;

    public PostCctvFileRes uploadCctvFile(String cctvImg) throws BaseException {
        Long adminIdx = jwtService.getAdminId();
        AdminEntity admin = adminRepository.findByAdminIdx(adminIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_ADMIN));
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

    public PostCctvLineRes uploadLine(Long lineNum) throws BaseException {
        Long adminIdx = jwtService.getAdminId();
        AdminEntity admin = adminRepository.findByAdminIdx(adminIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_ADMIN));
        // cctvIdx 조회 후 저장
        CctvEntity cctv = cctvRepository.findByAdminIdx(admin)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_CCTV));
        cctv.uploadLine(lineNum);
        cctvRepository.save(cctv);
        return new PostCctvLineRes(lineNum);
    }

    public PostCctvPosRes uploadPos(PostCctvPosListReq posReq) throws BaseException {
        Long adminIdx = jwtService.getAdminId();
        AdminEntity admin = adminRepository.findByAdminIdx(adminIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_ADMIN));
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
}
