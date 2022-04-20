package com.example.demo.util;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Map;

@Data
@Builder
public class ErrorHandling {
private Timestamp timestamp;
private String path;
private String message;
private Map<String,String> validationError;

}
