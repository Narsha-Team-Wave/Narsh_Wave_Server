package com.narsha.wave.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter @AllArgsConstructor
@Builder
public class UserDataResponse {

    private String name;

    private String gender;

    private int number;

    private String explain;

    private String stressState;

    private String healthState;

    private String mentalState;

    private int score;

    private int stress;

    private int health;

    private int mental;

    private String createdAt;

}
