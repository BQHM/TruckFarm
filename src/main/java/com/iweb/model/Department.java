package com.iweb.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    private String departmentId;
    private String departmentName;
    private Integer count;
    private Double fund;


}
