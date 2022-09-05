package com.narsha.wave.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


import java.util.Map;

@Mapper
public interface BaseDataMapper {

    int baseDataInput(Map<String, Object> params);
    int cursRslt(Map<String, Object> params);


}