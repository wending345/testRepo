package com.tongji.enso.mybatisdemo.service.online;

import com.tongji.enso.mybatisdemo.entity.online.Observation;
import com.tongji.enso.mybatisdemo.mapper.online.ObservationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObservationService {

    @Autowired
    private ObservationMapper observationMapper;

    public Observation findByYearAndMonth(int year, int month){
        return observationMapper.findByYearAndMonth(year, month);
    }

    public List<Observation> findAfterYearAndMonth(int year, int month){
        return observationMapper.findAllAfterYearAndMonth(year, month);
    }

}
