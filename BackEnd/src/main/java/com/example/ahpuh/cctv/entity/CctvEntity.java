package com.example.ahpuh.cctv.entity;

import com.example.ahpuh.admin.entity.AdminEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cctv")
public class CctvEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cctvIdx;

    @ManyToOne
    @JoinColumn(name = "adminIdx")
    private AdminEntity adminIdx;

    @Column(nullable = false)
    private Long lineNum;

    @Column(nullable = false, columnDefinition = "text")
    private String cctvFile;

    @Column(nullable = false, columnDefinition = "text")
    private String cctvImage;

    @Column(nullable = false, columnDefinition = "varchar(10) default 'active'")
    private String status;
}
