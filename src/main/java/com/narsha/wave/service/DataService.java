package com.narsha.wave.service;

import com.narsha.wave.domain.dto.request.SaveDataRequest;
import com.narsha.wave.domain.dto.response.DataResponse;
import com.narsha.wave.domain.dto.response.UserDataResponse;
import com.narsha.wave.domain.entity.Data;
import com.narsha.wave.domain.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;

    @Transactional
    public Long saveData(SaveDataRequest request) {
        Data data = Data.builder()
                .deviceId(request.getDeviceId())
                .delta(request.getDelta())
                .high_alpha(request.getHigh_alpha())
                .high_beta(request.getHigh_beta())
                .low_alpha(request.getLow_alpha())
                .low_beta(request.getLow_beta())
                .low_gamma(request.getLow_gamma())
                .mid_gamma(request.getMid_gamma())
                .theta(request.getTheta())
                .build();

        log.info("save data");
        return dataRepository.save(data).getIdx();
    }

    @Transactional(readOnly = true)
    public DataResponse AverageData(int deviceId) {
        Data avgData = dataRepository.findAvgByDeviceId(deviceId)
                .orElseThrow(RuntimeException::new);

        return DataResponse.builder()
                .deviceId(avgData.getDeviceId())
                .delta(avgData.getDelta())
                .high_alpha(avgData.getHigh_alpha())
                .high_beta(avgData.getHigh_beta())
                .low_alpha(avgData.getLow_alpha())
                .low_beta(avgData.getLow_beta())
                .low_gamma(avgData.getLow_gamma())
                .mid_gamma(avgData.getMid_gamma())
                .theta(avgData.getTheta())
                .build();
    }

    @Transactional(readOnly = true)
    public UserDataResponse getUserData(int deviceId, String name, String gender) {
        Data data = dataRepository.findFirstByDeviceIdOrderBySaveTimeDesc(deviceId)
                .orElseThrow(() -> {throw new RuntimeException("정보가 없습니다.");});

        // 스트레스, 정신 : alpha, beta
        // 건강 : gamma, delta, alpha

        // 스트레스 = high_alpha + high_beta / 48000 * 100
        // 건강 = mid_gamma + delta + high_alpha / 1284000 * 100
        // 정신 = mid_gamma + high_alpha _ high_beta / 408000 * 100
        // 총 점수 = 스트레스 + 건강 + 정신 / 1740000 * 100
        // high_alpha = 값 / 24000 * 100
        // low_alpha = 값 / 30000 * 100
        // high_beta = 값 / 24000 * 100
        // low_beta = 값 / 58000 * 100
        // mid_gamma = 값 / 360000 * 100
        // low_gamma = 값 / 32000 * 100
        // delta = 값 / 900000 * 100
        // theta = 값 / 300000 * 100

        int stressCheck = stressCheck(data.getHigh_alpha(), data.getLow_alpha());
        int healthCheck = healthCheck(data.getMid_gamma(), data.getLow_gamma(), data.getDelta(), data.getHigh_alpha());
        int mentalCheck = mentalCheck(data.getHigh_beta(), data.getLow_beta(), data.getHigh_alpha(), data.getLow_alpha());

        int stressScore = 100 - (((data.getHigh_alpha() + data.getHigh_beta()) * 100) / 48000);
        int healthScore = ((data.getMid_gamma() + data.getDelta() + data.getHigh_alpha()) * 100) / 1284000;
        int mentalScore = ((data.getMid_gamma() + data.getHigh_alpha() + data.getHigh_beta()) * 100) / 408000;
        int totalScore = (stressScore + healthScore + mentalScore) / 3;

        return UserDataResponse.builder()
                .name(name).gender(gender)
                .number(data.getDeviceId())
                .explain(checkExplain(totalScore)).score(totalScore)
                .stress(stressScore)
                .health(healthScore)
                .mental(mentalScore)
                .stressState(stressCheck == 2 ? "좋지않음" : stressCheck == 1 ? "보통" : "좋음")
                .healthState(healthCheck == 2 ? "좋지않음" : healthCheck == 1 ? "보통" : "좋음")
                .mentalState(mentalCheck == 2 ? "좋지않음" : mentalCheck == 1 ? "보통" : "좋음")
                .createdAt(DateTimeFormatter.ofPattern("yyyy.MM.dd").format(LocalDate.now()))
                .build();
    }

    /**
     * 설명
     * */
    public String checkExplain(int totalScore) {
        if(totalScore >= 70) {
            return "당신의 상태는 위험합니다.";
        } else if(totalScore >= 40) {
            return "당신의 상태는 보통입니다.";
        } else {
            return "당신의 상태는 매우 좋습니다.";
        }

    }

    /**
     * 스트레스 지수 체크
     * 2 : 높음
     * 1 : 보통
     * 0 : 낮음
    * */
    public int stressCheck(int high_alpha, int low_alpha) {
        if (high_alpha < 9500 && low_alpha < 8000) {
            return 2;
        } else if ((high_alpha > 9500 && high_alpha < 12000) || (low_alpha > 7000 && low_alpha < 9000)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 건강 샅애 지수 체크
     * 2 : 좋지 않음
     * 1 : 보통
     * 0 : 좋음
     * */
    public int healthCheck(int mid_gamma, int low_gamma, int delta, int high_alpha) {
        if(mid_gamma > 8000 && low_gamma > 7000) {
            return 2;
        } else if(delta > 300000 && high_alpha > 11000) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * 정신 상태 지수 체크
     * 2 : 좋지 않음
     * 1 : 보통
     * 0 : 좋음
     * */
    public int mentalCheck(int high_beta, int low_beta, int high_alpha, int low_alpha) {
        if(high_beta > 13000 && low_beta > 15750) {
            return 2;
        } else if((high_alpha > 9500 && high_alpha < 12000) ||
                (low_alpha > 7000 && low_alpha < 9000) ||
                (high_beta > 10000 && high_beta < 20000) ||
                (low_beta > 9000 && low_beta < 14000)) {
            return 1;
        } else {
            return 0;
        }
    }

}
