package com.narsha.wave.controller;

import com.narsha.wave.domain.entity.Data;
import com.narsha.wave.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class DataController {

//    private DataService dataService;

//    @RequestMapping(value = "/data", method = RequestMethod.GET)
//    public Optional<Data> data (
//            @RequestBody String data
//    ) {
//        return dataService.dataSelect(data);
//    }

}