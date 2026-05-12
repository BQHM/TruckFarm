package com.iweb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private boolean success;
    private String message;
    private Object data;

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
