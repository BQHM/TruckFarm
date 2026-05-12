package com.iweb.dto;

import lombok.Data;

@Data
public class PageDto {
    private int index;
    private int size;
    private String filed;
    private String order;
}
