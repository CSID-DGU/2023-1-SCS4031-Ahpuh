package com.example.ahpuh.cctv.entity;

import com.example.ahpuh.util.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name = "position")
public class PositionEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long positionIdx;

    @ManyToOne
    @JoinColumn(name = "cctvIdx")
    private CctvEntity cctvIdx;

    @Column(nullable = false)
    private Float xPos;

    @Column(nullable = false)
    private Float yPos;

    @Column(columnDefinition = "varchar(10) default 'ACTIVE'")
    private String status;
}
