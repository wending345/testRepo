package com.tongji.enso.mybatisdemo.mapper.online;


import com.tongji.enso.mybatisdemo.entity.online.Ice_forecast;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Ice_forecastMapper {

    /***
     * 根据年查找
     * @param year
     * @return
     */
    Ice_forecast findByYear(@Param("year") int year);
}
