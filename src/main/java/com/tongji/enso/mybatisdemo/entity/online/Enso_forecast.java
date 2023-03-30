package com.tongji.enso.mybatisdemo.entity.online;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enso_forecast {

    private int year;
    private int month;
    private String model;
    private String result;
}
