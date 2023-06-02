package com.example.ahpuh.user.repository;

import com.example.ahpuh.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <UserEntity, Long>{
    Optional<UserEntity> findByUserIdx(Long userIdx);
}
