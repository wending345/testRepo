package com.tongji.enso.mybatisdemo.controller.online;


import com.tongji.enso.mybatisdemo.entity.online.Enso_result;
import com.tongji.enso.mybatisdemo.entity.utils.Enso_respon_item;
import com.tongji.enso.mybatisdemo.service.online.Enso_resultService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/enso_origin")
public class Enso_resultController {

    @Autowired
    private Enso_resultService enso_resultService;

    //String2arr
    public ArrayList<Double> myString2Arr(String str){
        String[] str_p = str.split(" ");
        ArrayList<Double> arr = new ArrayList<>();
        for (String s : str_p) {
            arr.add(Double.parseDouble(s));
        }
        return arr;
    }

    /**
     * 生成日期串的方法
     * 参数：year month
     */
    public ArrayList<String> getDateList(int year, int month) {
        HashMap<Integer, String> monthMap = new HashMap<>();
        monthMap.put(1, "Jan");
        monthMap.put(2, "Feb");
        monthMap.put(3, "Mar");
        monthMap.put(4, "Apr");
        monthMap.put(5, "May");
        monthMap.put(6, "Jun");
        monthMap.put(7, "Jul");
        monthMap.put(8, "Aug");
        monthMap.put(9, "Sep");
        monthMap.put(10, "Oct");
        monthMap.put(11, "Nov");
        monthMap.put(12, "Dec");
        HashMap<Integer, String> yearMap = new HashMap<>();
        yearMap.put(2022, "22");
        yearMap.put(2023, "23");
        yearMap.put(2024, "24");
        yearMap.put(2025, "25");

        Enso_result es = enso_resultService.findByYearAndMonth(year, month);
        int length = myString2Arr(es.getEnso_cross()).size();
        System.out.println(length);
        int i = 1;
        int m = month;
        int y = year;
        ArrayList<String> output = new ArrayList<>();

        while (i <= length) {
            String str_y = yearMap.get(y);
            String str_m = monthMap.get(m);
            if (m == 12) {
                m = 1;
                y++;
            } else {
                m++;
            }
            i++;
            output.add(str_m + "-" + str_y);
        }
        return output;
    }

//    @GetMapping("/findByYearAndMonth")
//    @ApiOperation(value = "查询全部预报结果", notes = "查询全部预报结果")
//    public HashMap<String, Object> findByYearAndMonth(@RequestParam(value = "year", required = true) int year,
//                                                      @RequestParam(value = "month", required = true) int month){
//        Enso_result es = enso_resultService.findByYearAndMonth(year, month);
//        HashMap<String, Object> ensoHashMap = new HashMap<String, Object>();
//        ensoHashMap.put("Year", year);
//        ensoHashMap.put("Month", month);
//        ensoHashMap.put("Enso_Cross", myString2Arr(es.getEnso_cross()));
//        ensoHashMap.put("Enso_ASC", myString2Arr(es.getEnso_asc()));
//        ensoHashMap.put("Enso_MC", myString2Arr(es.getEnso_mc()));
//        ensoHashMap.put("Ensemble_Forecast", myString2Arr(es.getEf()));
//
//        return ensoHashMap;
//    }


//    //pre_step:先传一张图片充数
//    @Autowired
//    private Enso_figService enso_figService;
//
//    @GetMapping("/findByYearAndMonthFig")
//    @ApiOperation(value = "查询全部预报结果Fig", notes = "查询全部预报结果Fig")
//    public ResponseEntity<byte[]> findByYearAndMonthFig(@RequestParam(value = "year", required = true) int year,
//                                                        @RequestParam(value = "month", required = true) int month){
//        Enso_fig es_fig = enso_figService.findByYearAndMonth(year, month);
//        byte[] bytesByStream = es_fig.getImg();
//        final HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
//        return new ResponseEntity<>(bytesByStream, headers, HttpStatus.OK);
//    }


    /**
     * new method: for 小程序
     * 带格式的Json返回；
     *
     */
    @GetMapping("/findByYearAndMonth")
    @ApiOperation(value = "查询全部预报结果", notes = "查询全部预报结果")
    public HashMap<String, Object> findByYearAndMonth(@RequestParam(value = "year", required = true) int year,
                                                      @RequestParam(value = "month", required = true) int month){
        Enso_result es = enso_resultService.findByYearAndMonth(year, month);
        HashMap<String, Object> ensoHashMap = new HashMap<String, Object>();

        //set XAxis and YAxis
//        String raw = "Oct-22 Nov-22 Dec-22 Jan-23 Feb-23 Mar-23 Apr-23 May-23 Jun-23 Jul-23 Aug-23 Sep-23 Oct-23 Nov-23 Dec-23 Jan-24 Feb-24";
//        List<String> data_xy = Arrays.asList(raw.split(" "));
        ArrayList<String> data_xy = getDateList(year, month);
        HashMap<String, Object> data_xy_hash = new HashMap<String, Object>();
        data_xy_hash.put("data", data_xy);
        ensoHashMap.put("xAxis", data_xy_hash);
        ensoHashMap.put("yAxis", data_xy_hash);

        //set series
        ArrayList<Enso_respon_item> ser_list = new ArrayList<>();
        Enso_respon_item ei_cross = new Enso_respon_item();
        Enso_respon_item ei_asc = new Enso_respon_item();
        Enso_respon_item ei_mc = new Enso_respon_item();
        Enso_respon_item ei_ef = new Enso_respon_item();

        ei_cross.setName("ENSO-Cross");
        ei_cross.setData(myString2Arr(es.getEnso_cross()));
        ei_cross.setType("line");
        ei_asc.setName("ENSO-ASC");
        ei_asc.setData(myString2Arr(es.getEnso_asc()));
        ei_asc.setType("line");
        ei_mc.setName("ENSO-MC");
        ei_mc.setData(myString2Arr(es.getEnso_mc()));
        ei_mc.setType("line");
        ei_ef.setName("EnsembleForecast");
        ei_ef.setData(myString2Arr(es.getEf()));
        ei_ef.setType("line");

        ser_list.add(ei_cross);
        ser_list.add(ei_asc);
        ser_list.add(ei_mc);
        ser_list.add(ei_ef);

        ensoHashMap.put("series", ser_list);

        return ensoHashMap;
    }
}
