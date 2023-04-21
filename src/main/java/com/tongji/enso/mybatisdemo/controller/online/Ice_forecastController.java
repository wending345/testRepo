package com.tongji.enso.mybatisdemo.controller.online;


import com.tongji.enso.mybatisdemo.entity.online.Ice_forecast;
import com.tongji.enso.mybatisdemo.entity.online.Ice_obs;
import com.tongji.enso.mybatisdemo.service.online.Ice_forecastService;
import com.tongji.enso.mybatisdemo.service.online.Ice_obsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/ice")
public class Ice_forecastController {


    @Autowired
    private Ice_forecastService ice_forecastService;

    @Autowired
    private Ice_obsService ice_obsService;


    //String to string arr
    public ArrayList<String> myString2Arr(String str){
        String[] str_p = str.split(" ");
        ArrayList<String> arr = new ArrayList<>();
        for (String s : str_p) {
            arr.add(s);
        }
        return arr;
    }

    @GetMapping("/prediction")
    @ApiOperation(value = "海冰预测", notes = "预测接口")
    public HashMap<String, Object> icePrediction(@RequestParam(value = "year", required = true) int year){

        //data preparing
        Ice_forecast ice_forecast = ice_forecastService.findByYear(year);
        List<Ice_obs> obs_last_year = ice_obsService.findByYear(year-1);
        List<Ice_obs> obs_this_year = ice_obsService.findByYear(year);

        //return
        HashMap<String, Object> returnHashMap = new HashMap<String, Object>();

        //pre source
        ArrayList<List<String>> source = new ArrayList<List<String>>();
        ArrayList<String> month = new ArrayList<>();
        for (int i=1; i<=12; i++){
            month.add(""+i+"月");
        }
        month.add(0,"month");
        source.add(month);
        ArrayList<String> prediction = myString2Arr(ice_forecast.getPre());
        ArrayList<String> mean = myString2Arr(ice_forecast.getMean());
        ArrayList<String> upper = myString2Arr(ice_forecast.getUpper());
        ArrayList<String> lower = myString2Arr(ice_forecast.getLower());
        prediction.add(0,"prediction");
        mean.add(0,"mean");
        upper.add(0,"upper");
        lower.add(0,"lower");
        source.add(prediction);
        source.add(mean);
        source.add(upper);
        source.add(lower);
        ArrayList<String> obs_last = new ArrayList<>();
        for(Ice_obs i_obs:obs_last_year){
            obs_last.add(i_obs.getRecord()+"");
        }
        obs_last.add(0,"obs"+(year-1));
        source.add(obs_last);
        ArrayList<String> obs_this = new ArrayList<>();
        for(Ice_obs i_obs:obs_this_year){
            obs_this.add(i_obs.getRecord()+"");
        }
        obs_this.add(0,"obs"+(year));
        source.add(obs_this);

        HashMap<String, Object> datasetHashMap = new HashMap<String, Object>();
        datasetHashMap.put("source", source);
        returnHashMap.put("dataset",datasetHashMap);

        HashMap<String, Object> xHashMap = new HashMap<String, Object>();
        xHashMap.put("type", "category");
        returnHashMap.put("xAxis", xHashMap);
        HashMap<String, Object> yHashMap = new HashMap<String, Object>();
        yHashMap.put("gridIndex", "0");
        yHashMap.put("scale", "true");
        returnHashMap.put("yAxis", yHashMap);

        ArrayList<HashMap> series = new ArrayList<HashMap>();
        for(int i = 0; i<source.size(); i++){
            HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
            itemHashMap.put("type", "line");
            itemHashMap.put("seriesLayoutBy", "row");
            series.add(itemHashMap);
        }
        returnHashMap.put("series", series);

        HashMap<String, Object> legend = new HashMap<String, Object>();
        ArrayList<String> data = new ArrayList<>();
//        data.add("month");
        data.add("prediction");
        data.add("mean");
        data.add("upper");
        data.add("lower");
        data.add("obs"+(year-1));
        data.add("obs"+(year));
        legend.put("data",data);
        returnHashMap.put("legend",legend);

        return returnHashMap;


    }



}
