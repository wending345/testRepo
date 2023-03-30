package com.tongji.enso.mybatisdemo.service.online;


import com.tongji.enso.mybatisdemo.entity.online.Enso_result;
import com.tongji.enso.mybatisdemo.mapper.online.Enso_resultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Enso_resultService {

    @Autowired
    private Enso_resultMapper enso_resultMapper;

    public Enso_result findByYearAndMonth(int year, int month){
        return enso_resultMapper.findByYearAndMonth(year, month);
    }

}
