package com.example.ahpuh.admin.entity;

import com.example.ahpuh.cctv.entity.CctvEntity;
import com.example.ahpuh.user.entity.UserEntity;
import com.example.ahpuh.util.BaseTimeEntity;
import com.example.ahpuh.util.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name = "admin")
public class AdminEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminIdx;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String pwd;

    @Column(nullable = false, length = 100)
    private String poolName;

    @Column(nullable = false, length = 100)
    private String poolNum;

    @Column(nullable = false, length = 100)
    private String poolAddress;

    @Column(length = 100)
    private String poolImg;

    @Column(columnDefinition = "varchar(10) default 'ACTIVE'")
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userIdx")
    private List<UserEntity> userEntities = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cctvIdx")
    private List<CctvEntity> cctvEntities = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime accessTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public AdminEntity(String email, String pwd, String poolName, String poolNum, String poolAddress, String poolImg, LocalDateTime accessTime, Role role){
        this.email = email;
        this.pwd = pwd;
        this.poolName = poolName;
        this.poolNum = poolNum;
        this.poolImg = poolImg;
        this.accessTime = accessTime;
        this.poolAddress = poolAddress;
        this.role = role;
    }

    public void access() {
        this.accessTime = LocalDateTime.now();
    }

    public void uploadImg(String poolImg) {
        this.poolImg = poolImg;
    }
}
