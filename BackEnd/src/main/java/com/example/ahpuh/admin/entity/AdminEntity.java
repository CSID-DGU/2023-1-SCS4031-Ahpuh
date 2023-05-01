package com.example.ahpuh.admin.entity;

import com.example.ahpuh.cctv.entity.CctvEntity;
import com.example.ahpuh.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admin")
public class AdminEntity {
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

    @Column(nullable = false, columnDefinition = "varchar(10) default 'active'")
    private String status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userIdx")
    private List<UserEntity> userEntities = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cctvIdx")
    private List<CctvEntity> cctvEntities = new ArrayList<>();

    @Builder
    public AdminEntity(String email, String pwd, String poolName, String poolNum, String poolAddress, String status){
        this.email = email;
        this.pwd = pwd;
        this.poolName = poolName;
        this.poolNum = poolNum;
        this.poolAddress = poolAddress;
        this.status = status;
    }
}
