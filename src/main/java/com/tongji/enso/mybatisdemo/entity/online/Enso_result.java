package com.tongji.enso.mybatisdemo.entity.online;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enso_result {

    private int year;
    private int month;
    private String enso_cross;
    private String enso_asc;
    private String enso_mc;
    private String ef;
}
