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
                .orElseThrow(() -> {throw new RuntimeException("????????? ????????????.");});

        // ????????????, ?????? : alpha, beta
        // ?????? : gamma, delta, alpha

        // ???????????? = high_alpha + high_beta / 48000 * 100
        // ?????? = mid_gamma + delta + high_alpha / 1284000 * 100
        // ?????? = mid_gamma + high_alpha _ high_beta / 408000 * 100
        // ??? ?????? = ???????????? + ?????? + ?????? / 1740000 * 100
        // high_alpha = ??? / 24000 * 100
        // low_alpha = ??? / 30000 * 100
        // high_beta = ??? / 24000 * 100
        // low_beta = ??? / 58000 * 100
        // mid_gamma = ??? / 360000 * 100
        // low_gamma = ??? / 32000 * 100
        // delta = ??? / 900000 * 100
        // theta = ??? / 300000 * 100

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
                .stressState(stressCheck == 2 ? "????????????" : stressCheck == 1 ? "??????" : "??????")
                .healthState(healthCheck == 2 ? "????????????" : healthCheck == 1 ? "??????" : "??????")
                .mentalState(mentalCheck == 2 ? "????????????" : mentalCheck == 1 ? "??????" : "??????")
                .createdAt(DateTimeFormatter.ofPattern("yyyy.MM.dd").format(LocalDate.now()))
                .build();
    }

    /**
     * ??????
     * */
    public String checkExplain(int totalScore) {
        if(totalScore >= 70) {
            return "????????? ????????? ???????????????.";
        } else if(totalScore >= 40) {
            return "????????? ????????? ???????????????.";
        } else {
            return "????????? ????????? ?????? ????????????.";
        }

    }

    /**
     * ???????????? ?????? ??????
     * 2 : ??????
     * 1 : ??????
     * 0 : ??????
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
     * ?????? ?????? ?????? ??????
     * 2 : ?????? ??????
     * 1 : ??????
     * 0 : ??????
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
     * ?????? ?????? ?????? ??????
     * 2 : ?????? ??????
     * 1 : ??????
     * 0 : ??????
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
