package com.example.ahpuh.user.entity;

import com.example.ahpuh.admin.entity.AdminEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @ManyToOne
    @JoinColumn(name = "adminIdx")
    private AdminEntity adminIdx;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String phoneNum;

    @Column(nullable = true)
    private String gender;

    @Column(nullable = true)
    private String age;

    @Column(nullable = true)
    private String address;

    @Column(nullable = false, columnDefinition = "varchar(10) default 'ACTIVE'")
    private String lectureStatus;

    @Column(nullable = false, columnDefinition = "varchar(10) default 'active'")
    private String status;

    @Builder
    public UserEntity(String name, String phoneNum, String gender, String age, String address, String lectureStatus, String status){
        this.name = name;
        this.phoneNum = phoneNum;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.lectureStatus = lectureStatus;
        this.status = status;
    }
}
