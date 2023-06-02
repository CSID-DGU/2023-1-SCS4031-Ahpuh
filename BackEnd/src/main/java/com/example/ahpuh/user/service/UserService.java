package com.example.ahpuh.user.service;

import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.user.dto.PostUserReq;
import com.example.ahpuh.user.entity.UserEntity;
import com.example.ahpuh.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public void enterMember(AdminEntity admin, PostUserReq userReq){
        UserEntity user = createMember(admin, userReq);
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
