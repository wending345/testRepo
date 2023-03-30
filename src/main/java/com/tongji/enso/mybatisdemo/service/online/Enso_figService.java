package com.tongji.enso.mybatisdemo.service.online;


import com.tongji.enso.mybatisdemo.entity.online.Enso_fig;
import com.tongji.enso.mybatisdemo.mapper.online.Enso_figMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Enso_figService {

    @Autowired
    private Enso_figMapper enso_figMapper;

    public Enso_fig findByYearAndMonth(int year, int month){
        return enso_figMapper.findByYearAndMonth(year, month);
    }
}
