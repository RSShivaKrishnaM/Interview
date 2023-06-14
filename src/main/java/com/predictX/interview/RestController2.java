package com.predictX.interview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RestController2 {


    @Autowired
    private MaxCharCounter maxCharCounter;

    private String endResult;
    @Autowired
    private InputStrings inputStrings;


    @GetMapping(value = "/")
    public String get() {
        return endResult;
    }

    @PostMapping(value = "/")
    public void put(@RequestBody InputStrings inputStrings) {
        endResult = maxCharCounter.mix(inputStrings.getStr1(), inputStrings.getStr2());
    }
}
