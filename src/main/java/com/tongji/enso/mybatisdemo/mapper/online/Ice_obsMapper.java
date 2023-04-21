package com.tongji.enso.mybatisdemo.mapper.online;
import com.tongji.enso.mybatisdemo.entity.online.Ice_obs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Ice_obsMapper {

    /***
     *
     * @param year
     * @return
     */
    List<Ice_obs> findByYear(@Param("year") int year);
}
