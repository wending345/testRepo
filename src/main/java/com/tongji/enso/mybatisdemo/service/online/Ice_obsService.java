package com.tongji.enso.mybatisdemo.service.online;
import com.tongji.enso.mybatisdemo.entity.online.Ice_obs;
import com.tongji.enso.mybatisdemo.mapper.online.Ice_obsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Ice_obsService {
    @Autowired
    private Ice_obsMapper ice_obsMapper;

    public List<Ice_obs> findByYear(int year){
        return ice_obsMapper.findByYear(year);
    }
}
