package com.tongji.enso.mybatisdemo.service.online;

import com.tongji.enso.mybatisdemo.entity.online.Prediction;
import com.tongji.enso.mybatisdemo.mapper.online.PredictionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionService {

    @Autowired
    private PredictionMapper predictionMapper;

    public List<Prediction> findAll(){
        return predictionMapper.findAll();
    }

    public Prediction findByYearAndMonth(int year, int month){
        return predictionMapper.findByYearAndMonth(year, month);
    }
}

