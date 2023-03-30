package com.tongji.enso.mybatisdemo.mapper.online;


import com.tongji.enso.mybatisdemo.entity.online.Enso_forecast;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Enso_forecastMapper {
    List<Enso_forecast> findAllByYearAndMonth(@Param("year") int year, @Param("month") int month);

    List<Enso_forecast> findAllByModel(@Param("model") String Model);
}
