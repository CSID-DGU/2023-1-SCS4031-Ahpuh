package com.example.ahpuh.admin.service;
import com.example.ahpuh.jwt.TokenProvider;
import com.example.ahpuh.jwt.dto.TokenDto;
import com.example.ahpuh.jwt.entity.RefreshTokenEntity;
import com.example.ahpuh.jwt.repository.RefreshTokenRepository;
import com.example.ahpuh.admin.dto.PostAdminReq;
import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.admin.repository.AdminRepository;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponseStatus;
import com.example.ahpuh.util.Role;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.ahpuh.util.ValidationRegex.isRegexEmail;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    public TokenDto signUp(PostAdminReq admin) throws BaseException {
        if(admin.getEmail() == null || admin.getPwd() == null){
            throw new BaseException(BaseResponseStatus.POST_ADMIN_EMPTY);
        }
        if(!isRegexEmail(admin.getEmail())){
            throw new BaseException(BaseResponseStatus.POST_ADMIN_INVALID_EMAIL);
        }
        if(isHaveEmail(admin.getEmail())){
            throw new BaseException(BaseResponseStatus.DUPLICATE_EMAIL);
        }
        String password = admin.getPwd();
        try{
            String encodedPwd = passwordEncoder.encode(admin.getPwd());
            admin.setPwd(encodedPwd);
        }catch (Exception e){
            throw new BaseException(BaseResponseStatus.PASSWORD_ENCRYPTION_ERROR);
        }
        AdminEntity adminEntity = AdminEntity.builder()
                .email(admin.getEmail())
                .pwd(admin.getPwd())
                .poolName(admin.getPoolName())
                .poolNum(admin.getPoolNum())
                .poolAddress(admin.getPoolAddress())
                .role(Role.ADMIN)
                .build();
        admin.setPwd(password);
        adminRepository.save(adminEntity);
        return token(admin);

    }

    public boolean isHaveEmail(String email) { return adminRepository.existsByEmail(email); }


    public TokenDto token(PostAdminReq user){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPwd());
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        // 4. RefreshToken 저장
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        // 5. 토큰 발급
        return tokenDto;
    }


    public TokenDto reissue(TokenDto tokenRequestDto) { //재발급
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByKeyId(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshTokenEntity newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }
}
