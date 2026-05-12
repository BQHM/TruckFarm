package com.iweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plants {

    private String plantsId;
    private String plantsName;
    private Integer growthCycle;
    private Double plantsPrice;
    private String plantsClass;

}
