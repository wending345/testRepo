package com.tongji.enso.mybatisdemo.entity.online;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enso_fig {
    private int year;
    private int month;
    private byte[] img;
}
