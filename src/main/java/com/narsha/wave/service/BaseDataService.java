package com.narsha.wave.service;

import com.narsha.wave.mapper.BaseDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BaseDataService {

    @Autowired
    BaseDataMapper baseDataMapper;

    public int baseDataInput(Map<String, Object> params){
        System.out.println("BaseDataService data : "+params);
        return baseDataMapper.baseDataInput( params);
    }

}
