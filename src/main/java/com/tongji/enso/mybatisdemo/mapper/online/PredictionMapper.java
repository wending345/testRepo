package com.tongji.enso.mybatisdemo.mapper.online;


import com.tongji.enso.mybatisdemo.entity.online.Prediction;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionMapper {

    /**
     * 查询全部预报结果
     */
    List<Prediction> findAll();

    /**
     * 根据年月查询结果
     * @param: year, month
     * @return: prediction.
     */
    Prediction findByYearAndMonth(@Param("year") int year, @Param("month") int month);
}


