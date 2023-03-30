package com.tongji.enso.mybatisdemo.controller.online;

import com.tongji.enso.mybatisdemo.entity.online.Prediction;
import com.tongji.enso.mybatisdemo.service.online.PredictionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prediction")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @GetMapping("/findAll")
    @ApiOperation(value = "查询全部预报结果", notes = "查询全部预报结果")
    public List<Prediction> findAll(){
        return predictionService.findAll();
    }

    @GetMapping("/findByYearAndMonth")
    @ApiOperation(value = "查询全部预报结果", notes = "查询全部预报结果")
    public ResponseEntity<byte[]> findByYearAndMonth(@RequestParam(value = "year", required = true) int year,
                                               @RequestParam(value = "month", required = true) int month){
        Prediction p = predictionService.findByYearAndMonth(year, month);
        byte[] bytesByStream = p.getResult();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(bytesByStream, headers, HttpStatus.OK);
    }
}
