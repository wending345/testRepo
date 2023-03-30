package com.tongji.enso.mybatisdemo.service.online;


import com.tongji.enso.mybatisdemo.entity.online.Enso_forecast;
import com.tongji.enso.mybatisdemo.mapper.online.Enso_forecastMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Enso_forecastService {

    @Autowired
    private Enso_forecastMapper enso_forecastMapper;

    public List<Enso_forecast> findAllByYearAndMonth(int year, int month){
        return enso_forecastMapper.findAllByYearAndMonth(year, month);
    }

    public List<Enso_forecast> findAllByModel(String model){
        return enso_forecastMapper.findAllByModel(model);
    }
}
