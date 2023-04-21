package com.tongji.enso.mybatisdemo.mapper.online;

import com.tongji.enso.mybatisdemo.entity.online.Observation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservationMapper {
    Observation findByYearAndMonth(@Param("year") int year, @Param("month") int month);
    List<Observation> findAllAfterYearAndMonth(@Param("year") int year, @Param("month") int month);
}
