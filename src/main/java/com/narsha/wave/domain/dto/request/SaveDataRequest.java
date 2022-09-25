package com.narsha.wave.domain.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class SaveDataRequest {

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

}
