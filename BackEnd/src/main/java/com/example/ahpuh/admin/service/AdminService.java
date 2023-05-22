package com.example.ahpuh.admin.service;
import com.example.ahpuh.admin.dto.*;
import com.example.ahpuh.cctv.entity.CctvEntity;
import com.example.ahpuh.cctv.entity.PositionEntity;
import com.example.ahpuh.cctv.repository.CctvRepository;
import com.example.ahpuh.cctv.repository.PositionRepository;
import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.admin.repository.AdminRepository;
import com.example.ahpuh.jwt.service.JwtService;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponseStatus;
import com.example.ahpuh.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.ahpuh.util.ValidationRegex.isRegexEmail;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final CctvRepository cctvRepository;
    private final JwtService jwtService;
    private final PositionRepository positionRepository;

    public void signUp(PostAdminReq admin) throws BaseException {
        if(admin.getEmail() == null || admin.getPwd() == null){
            throw new BaseException(BaseResponseStatus.POST_ADMIN_EMPTY);
        }
        if(!isRegexEmail(admin.getEmail())){
            throw new BaseException(BaseResponseStatus.POST_ADMIN_INVALID_EMAIL);
        }
        if(isHaveEmail(admin.getEmail())){
            throw new BaseException(BaseResponseStatus.DUPLICATE_EMAIL);
        }
        AdminEntity adminEntity = AdminEntity.builder()
                .email(admin.getEmail())
                .pwd(admin.getPwd())
                .poolName(admin.getPoolName())
                .poolNum(admin.getPoolNum())
                .poolAddress(admin.getPoolAddress())
                .accessTime(LocalDateTime.now())
                .role(Role.ADMIN)
                .build();
        adminRepository.save(adminEntity);

    }

    public PostAdminRes signIn(PostLoginReq admin) throws BaseException {
        if(!isRegexEmail(admin.getEmail())){
            throw new BaseException(BaseResponseStatus.POST_ADMIN_INVALID_EMAIL);
        }

        Optional<AdminEntity> optional = Optional.ofNullable(adminRepository.findByEmail(admin.getEmail())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.FAILED_TO_LOGIN)));
        AdminEntity adminEntity = optional.get();

        if(admin.getPwd().equals(adminEntity.getPwd())) {
            String token = jwtService.createJwt(adminEntity.getAdminIdx());
            return new PostAdminRes(adminEntity.getAdminIdx(), token);
        }
        else{
            throw new BaseException(BaseResponseStatus.POST_USERS_INVALID_PASSWORD);
        }
    }

    public boolean isHaveEmail(String email) { return adminRepository.existsByEmail(email); }


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
