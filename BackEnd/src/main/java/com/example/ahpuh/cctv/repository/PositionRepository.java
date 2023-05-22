package com.example.ahpuh.cctv.repository;

import com.example.ahpuh.cctv.entity.CctvEntity;
import com.example.ahpuh.cctv.entity.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Long> {
    Optional<PositionEntity> findByCctvIdx(CctvEntity cctvIdx);
}
