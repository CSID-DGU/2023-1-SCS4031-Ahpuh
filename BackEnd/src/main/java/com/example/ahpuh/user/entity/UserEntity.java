package com.example.ahpuh.user.entity;

import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.util.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name = "user")
public class UserEntity extends BaseTimeEntity {
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

    @Column(columnDefinition = "varchar(10) default 'ACTIVE'")
    private String lectureStatus;

    @Column(columnDefinition = "varchar(10) default 'ACTIVE'")
    private String status;

    @Builder
    public UserEntity(String name, String phoneNum, String lectureStatus, String status){
        this.name = name;
        this.phoneNum = phoneNum;
        this.lectureStatus = lectureStatus;
        this.status = status;
    }
}
