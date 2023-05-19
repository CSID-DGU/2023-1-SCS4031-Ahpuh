package com.example.ahpuh.cctv.repository;

import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.cctv.entity.CctvEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CctvRepository extends JpaRepository<CctvEntity, Long> {
    Optional<CctvEntity> findByAdminIdx(AdminEntity admin);
}
