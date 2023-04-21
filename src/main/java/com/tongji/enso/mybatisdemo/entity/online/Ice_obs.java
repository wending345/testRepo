package com.tongji.enso.mybatisdemo.entity.online;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ice_obs {
    private int year;
    private int month;
    private double record;
}
