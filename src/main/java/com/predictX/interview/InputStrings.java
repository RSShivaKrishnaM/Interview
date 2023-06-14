package com.predictX.interview;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class InputStrings implements Serializable {
    @JsonProperty("string")
    private String str1;
    @JsonProperty("string2")
    private String str2;

    public String getStr1() {
        return str1;
    }

    public String getStr2() {
        return str2;
    }
}
