package com.tongji.enso.mybatisdemo.entity.online;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Prediction {

    private int year;
    private int month;
    private byte[] result;
}
