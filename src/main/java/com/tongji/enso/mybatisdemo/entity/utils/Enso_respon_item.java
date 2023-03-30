package com.tongji.enso.mybatisdemo.entity.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enso_respon_item {
    private String name;
    private ArrayList<Double> data;
    private String type;
}
