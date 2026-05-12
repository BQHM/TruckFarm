package com.iweb.dto;

import lombok.Data;

import java.util.List;

@Data
public class DataGrid<T> {
    private int total;
    private List<T> data;
}
