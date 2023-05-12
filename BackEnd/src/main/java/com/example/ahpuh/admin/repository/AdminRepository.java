package com.example.ahpuh.admin.repository;

import com.example.ahpuh.admin.entity.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    AdminEntity findAllByAdminIdx(Long userIdx);
    boolean existsByEmail(String email);
    Optional<AdminEntity> findByEmail(String email);
    Optional<AdminEntity> findByAdminIdx(Long adminIdx);
    Optional<AdminEntity> findByAdminIdxAndStatus(Long adminIdx, String status);
}
