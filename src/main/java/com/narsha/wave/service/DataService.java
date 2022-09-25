package com.narsha.wave.service;

import com.narsha.wave.domain.dto.request.SaveDataRequest;
import com.narsha.wave.domain.dto.response.DataResponse;
import com.narsha.wave.domain.entity.Data;
import com.narsha.wave.domain.repository.DataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataService {

    private final DataRepository dataRepository;

    @Transactional
    public Long saveData(SaveDataRequest request) {
        Data data = Data.builder()
                .deviceId(request.getDeviceId())
                .attention(request.getAttention())
                .delta(request.getDelta())
                .high_alpha(request.getHigh_alpha())
                .high_beta(request.getHigh_beta())
                .low_alpha(request.getLow_alpha())
                .low_beta(request.getLow_beta())
                .low_gamma(request.getLow_gamma())
                .meditation(request.getMeditation())
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
                .attention(avgData.getAttention())
                .delta(avgData.getDelta())
                .high_alpha(avgData.getHigh_alpha())
                .high_beta(avgData.getHigh_beta())
                .low_alpha(avgData.getLow_alpha())
                .low_beta(avgData.getLow_beta())
                .low_gamma(avgData.getLow_gamma())
                .meditation(avgData.getMeditation())
                .mid_gamma(avgData.getMid_gamma())
                .theta(avgData.getTheta())
                .build();
    }

}
