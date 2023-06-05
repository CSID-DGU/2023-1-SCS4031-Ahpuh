package com.example.ahpuh.user.service;

import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.user.dto.*;
import com.example.ahpuh.user.entity.UserEntity;
import com.example.ahpuh.user.repository.UserRepository;
import com.example.ahpuh.util.BaseException;
import com.example.ahpuh.util.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        UserEntity user = userRepository.findByUserIdxAndStatus(userIdx, "ACTIVE")
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_USER));
        if(!isRegexPhoneNum(userReq.getPhoneNum())){
            throw new BaseException(BaseResponseStatus.INVALID_USER_PHONENUM);
        }
        user.modifyInfo(userReq.getName(), userReq.getPhoneNum(), userReq.getAge(), userReq.getGender(), userReq.getBirth(), userReq.getParentalContacts());
        userRepository.save(user);
    }

    public void deleteMember(Long userIdx) throws BaseException {
        UserEntity user = userRepository.findByUserIdxAndStatus(userIdx, "ACTIVE")
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_USER));
        user.deleteMember(" ", " ", null, "INACTIVE");
        userRepository.save(user);
    }

    public List<GetUserRes> getMemberList(AdminEntity admin) throws BaseException {
        List<UserEntity> userList = userRepository.findByAdminIdxAndStatus(admin, "ACTIVE")
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_USER));
        List<GetUserRes> resList = new ArrayList<>();
        for (UserEntity i : userList){
            GetUserRes user = new GetUserRes(i.getName(), i.getPhoneNum(), i.getAge(),
                    i.getGender(), i.getBirth(), i.getParentalContacts());
            resList.add(user);
        }
        return resList;
    }

    public GetUserRes getMember(Long userIdx) throws BaseException {
        UserEntity user = userRepository.findByUserIdxAndStatus(userIdx, "ACTIVE")
                .orElseThrow(()-> new BaseException(BaseResponseStatus.NOT_FIND_USER));
        GetUserRes res = new GetUserRes(user.getName(), user.getPhoneNum(), user.getAge(),
                user.getGender(), user.getBirth(), user.getParentalContacts());
        return res;
    }

    public UserEntity createMember(AdminEntity admin, PostUserReq user){
        return UserEntity.builder()
                .adminIdx(admin)
                .name(user.getName())
                .phoneNum(user.getPhoneNum())
                .age(user.getAge())
                .gender(user.getGender())
                .birth(user.getBirth())
                .parentalContacts(user.getParentalContacts())
                .build();
    }
}
