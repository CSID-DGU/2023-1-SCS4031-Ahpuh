package com.example.ahpuh.user.service;

import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.user.dto.PatchLectureReq;
import com.example.ahpuh.user.dto.PatchUserReq;
import com.example.ahpuh.user.dto.PostUserRes;
import com.example.ahpuh.user.dto.PostUserReq;
import com.example.ahpuh.user.entity.UserEntity;
import com.example.ahpuh.user.repository.UserRepository;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.ahpuh.util.ValidationRegex.isRegexPhoneNum;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public PostUserRes enterMember(AdminEntity admin, PostUserReq userReq) throws BaseException {
        if(!isRegexPhoneNum(userReq.getPhoneNum())){
            throw new BaseException(BaseResponseStatus.INVALID_USER_PHONENUM);
        }
        UserEntity user = createMember(admin, userReq);
        userRepository.save(user);
        return new PostUserRes(user.getUserIdx(), "회원 등록을 완료하였습니다.");
    }

    public void modifyMember(Long userIdx, PatchUserReq userReq) throws BaseException {
        UserEntity user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_USER));
        if(!isRegexPhoneNum(userReq.getPhoneNum())){
            throw new BaseException(BaseResponseStatus.INVALID_USER_PHONENUM);
        }
        user.modifyInfo(userReq.getName(), userReq.getPhoneNum(), userReq.getGender(), userReq.getAge(), userReq.getAddress());
        userRepository.save(user);
    }

    public void modifyMemberStatus(Long userIdx, PatchLectureReq lectureReq) throws BaseException {
        UserEntity user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_USER));
        user.modifyLecStatus(lectureReq.getLectureStatus());
        userRepository.save(user);
    }

    public void deleteMember(Long userIdx) throws BaseException {
        UserEntity user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_USER));
        user.deleteMember(" ", " ", null, "INACTIVE", "INACTIVE");
        userRepository.save(user);
    }

    public UserEntity createMember(AdminEntity admin, PostUserReq user){
        return UserEntity.builder()
                .adminIdx(admin)
                .name(user.getName())
                .phoneNum(user.getPhoneNum())
                .age(user.getAge())
                .gender(user.getGender())
                .address(user.getAddress())
                .build();
    }
}
