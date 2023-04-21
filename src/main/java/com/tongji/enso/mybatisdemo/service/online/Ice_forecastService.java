package com.tongji.enso.mybatisdemo.service.online;
import com.tongji.enso.mybatisdemo.entity.online.Ice_forecast;
import com.tongji.enso.mybatisdemo.mapper.online.Ice_forecastMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Ice_forecastService {

    @Autowired
    private Ice_forecastMapper ice_forecastMapper;

    public Ice_forecast findByYear(int year){
        return ice_forecastMapper.findByYear(year);
    }
}
