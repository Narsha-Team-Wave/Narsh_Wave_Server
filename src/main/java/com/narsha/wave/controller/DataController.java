package com.narsha.wave.controller;

import com.narsha.wave.domain.dto.request.SaveDataRequest;
import com.narsha.wave.domain.dto.response.DataResponse;
import com.narsha.wave.domain.dto.response.UserDataResponse;
import com.narsha.wave.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(tags = "JPA")
@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController {

    private final DataService dataService;

    @ApiOperation("저장")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    public Long saveData (
            @RequestBody SaveDataRequest request
    ) {
        return dataService.saveData(request);
    }

    @ApiOperation("평균값")
    @GetMapping("/{device-id}")
    public DataResponse getAvgData(
            @PathVariable("device-id") int deviceId
    ) {
        return dataService.AverageData(deviceId);
    }

    @ApiOperation("유저 정보")
    @GetMapping("/user/{device-id}")
    public UserDataResponse getUserData(
            @PathVariable("device-id") int deviceId,
            @RequestParam("name") String name,
            @RequestParam("gender") String gender
    ) {
        return dataService.getUserData(deviceId, name, gender);
    }



}