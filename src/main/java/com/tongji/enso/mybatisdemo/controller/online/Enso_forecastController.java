package com.tongji.enso.mybatisdemo.controller.online;

import com.tongji.enso.mybatisdemo.entity.online.Enso_forecast;
import com.tongji.enso.mybatisdemo.entity.online.Enso_result;
import com.tongji.enso.mybatisdemo.entity.online.Observation;
import com.tongji.enso.mybatisdemo.service.online.Enso_forecastService;
import com.tongji.enso.mybatisdemo.service.online.ObservationService;
import com.tongji.enso.mybatisdemo.service.online.Enso_resultService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author cyx
 * @time 2023.03.29
 */

@RestController
@RequestMapping("/enso")
public class Enso_forecastController {

    @Autowired
    private Enso_forecastService enso_forecastService;

    @Autowired
    private ObservationService observationService;

    /**
     * utils functions
     * @param str
     * @return
     */
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
     * 参数：null
     */
    public ArrayList<String> getDateList(int y, int m, int length) {
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

        int i = 1;
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


    /**
     * 主页面;
     * 参数：年月year and month
     */
    @GetMapping("/mainPage")
    @ApiOperation(value = "主页面：模型预报对比", notes = "模型预报对比")
    public HashMap<String, Object> mainPage(@RequestParam(value = "year", required = true) int year,
                                            @RequestParam(value = "month", required = true) int month){

        //数据准备
        List<Enso_forecast> efs = enso_forecastService.findAllByYearAndMonth(year, month);
        List<Observation> obs = observationService.findAll();
        int len = obs.size();
        //System.out.println();
        HashMap<String, Object> return_HashMap = new HashMap<String, Object>();

        //官方数据
        ArrayList<Double> nino = new ArrayList<Double>();
        for(Observation ob:obs){
            nino.add(ob.getRecord());
        }

        //series
        ArrayList<HashMap<String, Object>> series = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i<efs.size();i++){
            HashMap<String, Object> series_i = new HashMap<String, Object>();
            series_i.put("name", efs.get(i).getModel());
            series_i.put("type","line");
            series_i.put("data", myString2Arr(efs.get(i).getResult()));
            series.add(series_i);
        }

        return_HashMap.put("series",series);

        //准备axis
        HashMap<String, Object> xHashMap = new HashMap<String, Object>();
        int y=efs.get(0).getYear(),m=efs.get(0).getMonth();
        int len_max = myString2Arr(efs.get(0).getResult()).size();
        for (int k = 0;k<efs.size();k++){
            if (myString2Arr(efs.get(k).getResult()).size() > len_max){
                len_max = myString2Arr(efs.get(k).getResult()).size();
            }
        }
        xHashMap.put("data", getDateList(y,m,len_max));
        return_HashMap.put("xAxis", xHashMap);

        return return_HashMap;
    }

    /**
     * 分析页面char 1;
     * 参数：模型类型 目前为ef ensemble forecast
     */
    @GetMapping("/analyzePage/chart1")
    @ApiOperation(value = "图表1：集合预报模型逐月起报结果", notes = "集合预报模型逐月起报结果")
    public HashMap<String, Object> chart1(@RequestParam(value = "model", required = true) String model){

        //数据准备
        List<Enso_forecast> efs = enso_forecastService.findAllByModel(model);
        List<Observation> obs = observationService.findAll();
        int len = obs.size();
        int y = obs.get(0).getYear();
        int m = obs.get(0).getMonth();
        //System.out.println();
        HashMap<String, Object> efHashMap = new HashMap<String, Object>();

        //数据二维字符串列表
        HashMap<String, Object> dataHashMap = new HashMap<String, Object>();
        ArrayList<List<String>> dataset = new ArrayList<List<String>>();
        //年月数据;
        ArrayList<String> date = getDateList(y,m,len);
        date.add(0, "month");
        dataset.add(date);
        //循环生成ef数据
        for(int i =0; i<len; i++){
            String[] tmp_ef_result = efs.get(i).getResult().split(" ");
            int tmp_year = efs.get(i).getYear();
            int tmp_month = efs.get(i).getMonth();
            ArrayList<String> ef_i = new ArrayList<String>();
            int j = 0;
            while (j<i){
                ef_i.add("");
                j++;
            }
            for(int k = 0; j<len; j++, k++){
                if (k < tmp_ef_result.length){
                    ef_i.add(tmp_ef_result[k]);
                } else {
                    break;
                }

            }
            String title = "";
            if (tmp_month==1){
                title = (tmp_year-1) + "年" + "12月起报";
            }else {
                title = tmp_year + "年" + (tmp_month-1) + "月起报";
            }

            ef_i.add(0, title);
            dataset.add(ef_i);
        }
        //官方数据
        ArrayList<String> nino = new ArrayList<String>();
        for (Observation ob:obs){
            nino.add(""+ob.getRecord());
        }
        nino.add(0,"气候中心Nino3.4指数记录");
        dataset.add(nino);

        dataHashMap.put("source", dataset);
        efHashMap.put("dataset", dataHashMap);

        //乱起八糟的要求
        HashMap<String, Object> xHashMap = new HashMap<String, Object>();
        xHashMap.put("type", "category");
        efHashMap.put("xAxis", xHashMap);
        HashMap<String, Object> yHashMap = new HashMap<String, Object>();
        yHashMap.put("type", "value");
        efHashMap.put("yAxis", yHashMap);

        ArrayList<HashMap> series = new ArrayList<HashMap>();
        for(int i = 0; i<len; i++){
            HashMap<String, Object> lineStyleHashMap = new HashMap<String, Object>();
            lineStyleHashMap.put("width", 2);
            lineStyleHashMap.put("type", "dotted");
            HashMap<String, Object> normalHashMap = new HashMap<String, Object>();
            normalHashMap.put("lineStyle", lineStyleHashMap);
            HashMap<String, Object> itemStyleHashMap = new HashMap<String, Object>();
            itemStyleHashMap.put("normal", normalHashMap);
            HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
            itemHashMap.put("itemStyle", itemStyleHashMap);
            itemHashMap.put("type", "line");
            itemHashMap.put("seriesLayoutBy", "row");

            series.add(itemHashMap);
        }
        //nino特殊格式
        HashMap<String, Object> lineStyleHashMap = new HashMap<String, Object>();
        lineStyleHashMap.put("width", 4);
        lineStyleHashMap.put("type", "solid");
        lineStyleHashMap.put("color", "black");
        HashMap<String, Object> normalHashMap = new HashMap<String, Object>();
        normalHashMap.put("lineStyle", lineStyleHashMap);
        normalHashMap.put("color", "black");
        HashMap<String, Object> itemStyleHashMap = new HashMap<String, Object>();
        itemStyleHashMap.put("normal", normalHashMap);
        HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
        itemHashMap.put("itemStyle", itemStyleHashMap);
        itemHashMap.put("type", "line");
        itemHashMap.put("seriesLayoutBy", "row");
        series.add(itemHashMap);

        efHashMap.put("series",series);

        return efHashMap;
    }


    /**
     * 分析页面char 2;
     * 参数：模型类型 目前为ef ensemble forecast
     */
    @GetMapping("/analyzePage/chart2")
    @ApiOperation(value = "图表2：分月展示预报与误差", notes = "分月展示预报与误差")
    public List<HashMap<String, Object>> chart2(@RequestParam(value = "model", required = true) String model){

        //数据准备
        List<Enso_forecast> efs = enso_forecastService.findAllByModel(model);
        List<Observation> obs = observationService.findAll();
        int len = obs.size();
        int y = obs.get(0).getYear();
        int m = obs.get(0).getMonth();
        //System.out.println();
        ArrayList<HashMap<String, Object>> return_HashMap = new ArrayList<HashMap<String, Object>>();

        //年月数据;
        ArrayList<String> date = getDateList(y,m,len);
        //官方数据
        ArrayList<Double> nino = new ArrayList<Double>();
        for(Observation ob:obs){
            nino.add(ob.getRecord());
        }
        //获取每个月的信息
        for (int i = 0; i<len; i++){
            HashMap<String, Object> item_HashMap = new HashMap<String, Object>();

            //准备source
            ArrayList<Double> result_i = myString2Arr(efs.get(i).getResult());
            ArrayList<String> bias_i = new ArrayList<String>();
            ArrayList<String> nino_i = new ArrayList<String>();
            ArrayList<String> date_i = new ArrayList<String>();
            ArrayList<String> ef_i = new ArrayList<String>();

            ArrayList<List<String>> source = new ArrayList<List<String>>();
            int j = 0;
            int k = 0;
            while (k<len){
                if (k<i){
                    k++;
                    continue;
                }else if (j<result_i.size()){
                    bias_i.add(""+Math.abs(result_i.get(j)-nino.get(k)));
                    nino_i.add(""+nino.get(k));
                    ef_i.add(""+result_i.get(j));
                    date_i.add(date.get(k));
                    k++;
                    j++;
                }else {
                    break;
                }
            }
            int year_i = efs.get(i).getYear();
            int month_i = efs.get(i).getMonth();
            String title_f = "";
            String title_b = "";
            if (month_i == 1){
                title_b = "" + (year_i-1) + "年12月起报误差";
                title_f = "" + (year_i-1) + "年12月起报";
            }else {
                title_b = "" + (year_i) + "年" + (month_i-1)+"月起报误差";
                title_f = "" + (year_i) + "年" + (month_i-1)+"月起报";
            }
            bias_i.add(0, title_b);
            ef_i.add(0,title_f);
            nino_i.add(0,"气候中心Nino3.4指数记录");
            date_i.add(0,"month");
            source.add(date_i);
            source.add(bias_i);
            source.add(ef_i);
            source.add(nino_i);


            //准备dataset
            HashMap<String, Object> dataset = new HashMap<String, Object>();
            dataset.put("source",source);
            item_HashMap.put("dataset",dataset);

            //准备axis
            HashMap<String, Object> xHashMap = new HashMap<String, Object>();
            xHashMap.put("type", "category");
            item_HashMap.put("xAxis",xHashMap);

            HashMap<String, Object> yHashMap = new HashMap<String, Object>();
            yHashMap.put("type", "value");
            item_HashMap.put("yAxis",yHashMap);

            //准备legend
            HashMap<String, Object> legend = new HashMap<String, Object>();
            item_HashMap.put("legend",legend);

            //准备series
            HashMap<String, Object> series_i = new HashMap<String, Object>();
            series_i.put("type","bar");
            series_i.put("seriesLayoutBy", "row");
            HashMap<String, Object> series_i2 = new HashMap<String, Object>();
            series_i2.put("type","line");
            series_i2.put("seriesLayoutBy", "row");
            ArrayList<HashMap<String, Object>> series = new ArrayList<HashMap<String, Object>>();
            series.add(series_i);
            series.add(series_i2);
            series.add(series_i2);

            item_HashMap.put("series",series);

            return_HashMap.add(item_HashMap);
        }

        return return_HashMap;
    }


    /**
     * 分析页面char 3;
     * 参数：模型类型 目前为ef ensemble forecast
     */
    @GetMapping("/analyzePage/chart3")
    @ApiOperation(value = "图表3：不同起报数据盒子图", notes = "不同起报数据盒子图")
    public HashMap<String, Object> chart3(@RequestParam(value = "model", required = true) String model){

        //数据准备
        List<Enso_forecast> efs = enso_forecastService.findAllByModel(model);
        List<Observation> obs = observationService.findAll();
        int len = obs.size();
        int y0 = obs.get(0).getYear();
        int m0 = obs.get(0).getMonth();
        //System.out.println();
        HashMap<String, Object> return_HashMap = new HashMap<String, Object>();

        //年月数据;
        ArrayList<String> date = getDateList(y0,m0,len);
        //官方数据
        ArrayList<Double> nino = new ArrayList<Double>();
        for(Observation ob:obs){
            nino.add(ob.getRecord());
        }

        ArrayList<HashMap<String, Object>> dataset_list = new ArrayList<HashMap<String, Object>>();
        //获取每个月的信息
        for (int i = 0; i<len; i++){
            HashMap<String, Object> item_HashMap = new HashMap<String, Object>();
            //准备source
            ArrayList<List<String>> source_i = new ArrayList<List<String>>();
            ArrayList<Double> result_i = myString2Arr(efs.get(i).getResult());
            ArrayList<String> bias_i = new ArrayList<String>();
            ArrayList<String> ef_i = new ArrayList<String>();
            int j = 0;
            int k = 0;
            while (k<len){
                if (k<i){
                    k++;
                    continue;
                }else if (j<result_i.size()) {
                    bias_i.add(""+Math.abs(result_i.get(j)-nino.get(k)));
                    ef_i.add(""+result_i.get(j));
                    k++;
                    j++;
                }else {
                    break;
                }
            }
            source_i.add(bias_i);
            HashMap<String, Object> source_i_hm = new HashMap<String, Object>();
            source_i_hm.put("source", source_i);
            dataset_list.add(source_i_hm);
        }

        //继续dataset
        for (int m = 0; m<len; m++){
            HashMap<String, Object> format_i = new HashMap<String, Object>();
            format_i.put("fromDatasetIndex",m);
            HashMap<String, Object> transform_i = new HashMap<String, Object>();
            transform_i.put("type","boxplot");
            format_i.put("transform", transform_i);
            dataset_list.add(format_i);
        }
        return_HashMap.put("dataset", dataset_list);

        //准备axis
        HashMap<String, Object> xHashMap = new HashMap<String, Object>();
        xHashMap.put("type", "category");
        HashMap<String, Object> axisLabel = new HashMap<String, Object>();
        xHashMap.put("axisLabel", axisLabel);
        return_HashMap.put("xAxis", xHashMap);


        HashMap<String, Object> yHashMap = new HashMap<String, Object>();
        yHashMap.put("type", "value");
        return_HashMap.put("yAxis", yHashMap);

        //series
        ArrayList<HashMap<String, Object>> series_list = new ArrayList<HashMap<String, Object>>();
        for (int k = 0; k<len; k++){
            HashMap<String, Object> series_i = new HashMap<String, Object>();
            int y = obs.get(k).getYear();
            int m = obs.get(k).getMonth();
            String name = "";
            if (m == 1){
                name = "" + (y-1) + "年12月起报预报误差";
            }else {
                name = "" + (y) + "年" + (m-1)+"月起报预报误差";
            }
            series_i.put("name",name);
            series_i.put("type","boxplot");
            series_i.put("datasetIndex", k+len);
            series_list.add(series_i);
        }
        return_HashMap.put("series", series_list);

        return return_HashMap;
    }

    /**
     * 分析页面char 4;
     * 参数：模型类型 目前为ef ensemble forecast
     */
    @GetMapping("/analyzePage/chart4")
    @ApiOperation(value = "图表4：相关系数分析", notes = "相关系数分析")
    public HashMap<String, Object> chart4(@RequestParam(value = "model", required = true) String model){

        //数据准备
        List<Enso_forecast> efs = enso_forecastService.findAllByModel(model);
        List<Observation> obs = observationService.findAll();
        int len = obs.size();
        //System.out.println();
        HashMap<String, Object> return_HashMap = new HashMap<String, Object>();

        //官方数据
        ArrayList<Double> nino = new ArrayList<Double>();
        for(Observation ob:obs){
            nino.add(ob.getRecord());
        }

        ArrayList<Double> pearson_cor = new ArrayList<Double>();
        //获取每个月的信息
        for (int i = 0; i<len; i++){
            //准备source
            ArrayList<Double> result_i = myString2Arr(efs.get(i).getResult());
            ArrayList<Double> nino_i = new ArrayList<Double>();
            ArrayList<Double> ef_i = new ArrayList<Double>();

            int j = 0;
            int k = 0;
            while (k<len){
                if (k<i){
                    k++;
                    continue;
                }else if (j<result_i.size()) {
                    nino_i.add(nino.get(k));
                    ef_i.add(result_i.get(j));
                    k++;
                    j++;
                } else {
                    break;
                }
            }
            double pearson_i = getPearsonCorrelationScore(ef_i, nino_i);
            pearson_cor.add(pearson_i);
        }
        //series
        HashMap<String, Object> data_HashMap = new HashMap<String, Object>();
        data_HashMap.put("data", pearson_cor);
        data_HashMap.put("type","line");
        ArrayList<HashMap<String, Object>> series = new ArrayList<HashMap<String, Object>>();
        series.add(data_HashMap);
        return_HashMap.put("series", series);

        //准备axis
        HashMap<String, Object> xHashMap = new HashMap<String, Object>();
        xHashMap.put("type", "category");
        HashMap<String, Object> axisLabel = new HashMap<String, Object>();
        axisLabel.put("rotate",50);
        xHashMap.put("axisLabel", axisLabel);
        ArrayList<String> data = new ArrayList<String>();
        for (int i = 0;i<len;i++){
            int y = obs.get(i).getYear();
            int m = obs.get(i).getMonth();
            String name = "";
            if (m == 1){
                name = "" + (y-1) + "年12月起报结果";
            }else {
                name = "" + (y) + "年" + (m-1)+"月起报结果";
            }
            data.add(name);
        }
        xHashMap.put("data",data);
        return_HashMap.put("xAxis", xHashMap);

        HashMap<String, Object> yHashMap = new HashMap<String, Object>();
        yHashMap.put("type", "value");
        return_HashMap.put("yAxis", yHashMap);


        return return_HashMap;
    }


    /**
     * 求pearson相关系数
     */
    public static double getPearsonCorrelationScore(ArrayList<Double> x, ArrayList<Double> y) {
        if (x.size() != y.size())
            throw new RuntimeException("数据不正确！");
        double[] xData = new double[x.size()];
        double[] yData = new double[x.size()];
        for (int i = 0; i < x.size(); i++) {
            xData[i] = x.get(i);
            yData[i] = y.get(i);
        }
        return getPearsonCorrelationScore(xData,yData);
    }

    public static double getPearsonCorrelationScore(double[] xData, double[] yData) {
        if (xData.length != yData.length)
            throw new RuntimeException("数据不正确！");
        double xMeans;
        double yMeans;
        double numerator = 0;// 求解皮尔逊的分子
        double denominator = 0;// 求解皮尔逊系数的分母

        double result = 0;
        // 拿到两个数据的平均值
        xMeans = getMeans(xData);
        yMeans = getMeans(yData);
        // 计算皮尔逊系数的分子
        numerator = generateNumerator(xData, xMeans, yData, yMeans);
        // 计算皮尔逊系数的分母
        denominator = generateDenomiator(xData, xMeans, yData, yMeans);
        // 计算皮尔逊系数
        result = numerator / denominator;
        return result;
    }

    /**
     * 计算分子
     *
     * @param xData
     * @param xMeans
     * @param yData
     * @param yMeans
     * @return
     */
    private static double generateNumerator(double[] xData, double xMeans, double[] yData, double yMeans) {
        double numerator = 0.0;
        for (int i = 0; i < xData.length; i++) {
            numerator += (xData[i] - xMeans) * (yData[i] - yMeans);
        }
        return numerator;
    }

    /**
     * 生成分母
     *
     * @param yMeans
     * @param yData
     * @param xMeans
     * @param xData
     * @return 分母
     */
    private static double generateDenomiator(double[] xData, double xMeans, double[] yData, double yMeans) {
        double xSum = 0.0;
        for (int i = 0; i < xData.length; i++) {
            xSum += (xData[i] - xMeans) * (xData[i] - xMeans);
        }
        double ySum = 0.0;
        for (int i = 0; i < yData.length; i++) {
            ySum += (yData[i] - yMeans) * (yData[i] - yMeans);
        }
        return Math.sqrt(xSum) * Math.sqrt(ySum);
    }

    /**
     * 根据给定的数据集进行平均值计算
     *
     * @param
     * @return 给定数据集的平均值
     */
    private static double getMeans(double[] datas) {
        double sum = 0.0;
        for (int i = 0; i < datas.length; i++) {
            sum += datas[i];
        }
        return sum / datas.length;
    }


}
