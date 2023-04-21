package com.tongji.enso.mybatisdemo.entity.online;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ice_forecast {
    private int year;
    private String pre;
    private String mean;
    private String upper;
    private String lower;
}
