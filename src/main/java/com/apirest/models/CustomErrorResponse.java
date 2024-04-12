package com.apirest.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CustomErrorResponse {
    private LocalDateTime timeStamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
