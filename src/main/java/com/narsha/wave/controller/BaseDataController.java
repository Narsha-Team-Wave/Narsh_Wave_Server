package com.narsha.wave.controller;
import com.narsha.wave.service.BaseDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BaseDataController {

    @Autowired
    BaseDataService baseDataService;

    @RequestMapping("/inputData")
    public @ResponseBody Map<String, Object> inputData(@RequestBody Map<String, Object> params) throws Exception{
        System.out.println("BaseDataController data : "+params);
        baseDataService.baseDataInput(params);
        Map<String, Object> responseData = new HashMap<>();

        return params;

    }

}
