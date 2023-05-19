package com.example.ahpuh.admin.service;
import com.example.ahpuh.admin.dto.*;
import com.example.ahpuh.cctv.entity.CctvEntity;
import com.example.ahpuh.cctv.repository.CctvRepository;
import com.example.ahpuh.jwt.repository.RefreshTokenRepository;
import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.admin.repository.AdminRepository;
import com.example.ahpuh.jwt.service.JwtService;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponseStatus;
import com.example.ahpuh.util.Role;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.ahpuh.util.ValidationRegex.isRegexEmail;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CctvRepository cctvRepository;
    private final JwtService jwtService;

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


    public PostCctvImgRes uploadCctvImg(String cctvImg) throws BaseException {
        Long adminIdx = jwtService.getAdminId();
        AdminEntity admin = adminRepository.findByAdminIdx(adminIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.NOT_FIND_ADMIN));
        createCCTV(admin, cctvImg);
        return new PostCctvImgRes(cctvImg);
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
}
