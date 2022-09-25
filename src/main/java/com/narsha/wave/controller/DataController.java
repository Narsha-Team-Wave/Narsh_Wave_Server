package com.narsha.wave.controller;

import com.narsha.wave.domain.dto.request.SaveDataRequest;
import com.narsha.wave.domain.dto.response.DataResponse;
import com.narsha.wave.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController {

    private final DataService dataService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public Long saveData (
            @RequestBody SaveDataRequest request
    ) {
        return dataService.saveData(request);
    }

    @GetMapping("/{device-id}")
    public DataResponse getAvgData(
            @PathVariable("device-id") int deviceId
    ) {
        return dataService.AverageData(deviceId);
    }

}