package com.example.ahpuh.cctv.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "position")
public class PositionEntity {
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

    @Column(nullable = false, columnDefinition = "varchar(10) default 'active'")
    private String status;
}
