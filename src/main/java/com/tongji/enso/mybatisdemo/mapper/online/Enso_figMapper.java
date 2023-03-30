package com.tongji.enso.mybatisdemo.mapper.online;


import com.tongji.enso.mybatisdemo.entity.online.Enso_fig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Enso_figMapper {
    /**
     * 根据年月查询结果
     * @param: year, month
     * @return: Enso_fig;
     */
    Enso_fig findByYearAndMonth(@Param("year") int year, @Param("month") int month);
}
