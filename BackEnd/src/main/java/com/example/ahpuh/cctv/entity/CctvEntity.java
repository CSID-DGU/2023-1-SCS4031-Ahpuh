package com.example.ahpuh.cctv.entity;

import com.example.ahpuh.admin.entity.AdminEntity;
import com.example.ahpuh.util.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Builder
@Table(name = "cctv")
public class CctvEntity extends BaseTimeEntity {

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

    @Column(columnDefinition = "varchar(10) default 'ACTIVE'")
    private String status;

    public void uploadImg(String cctvImage) {
        this.cctvImage = cctvImage;
    }

    public void uploadLine(Long lineNum){
        this.lineNum = lineNum;
    }
}
