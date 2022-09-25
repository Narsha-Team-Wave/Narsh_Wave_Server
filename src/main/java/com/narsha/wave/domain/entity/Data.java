package com.narsha.wave.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb1_noepa_data")
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @CreationTimestamp
    private LocalDateTime saveTime;

    private int deviceId;

    private int attention;
    private int delta;
    private int high_alpha;
    private int high_beta;
    private int low_alpha;
    private int low_beta;
    private int low_gamma;
    private int meditation;
    private int mid_gamma;
    private int theta;

    @Builder
    public Data(int deviceId, int attention, int delta, int high_alpha, int high_beta, int low_alpha, int low_beta, int low_gamma, int meditation, int mid_gamma, int theta) {
        this.deviceId = deviceId;
        this.attention = attention;
        this.delta = delta;
        this.high_alpha = high_alpha;
        this.high_beta = high_beta;
        this.low_alpha = low_alpha;
        this.low_beta = low_beta;
        this.low_gamma = low_gamma;
        this.meditation = meditation;
        this.mid_gamma = mid_gamma;
        this.theta = theta;
    }
}