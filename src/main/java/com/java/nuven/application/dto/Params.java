package com.java.nuven.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Params {
    private int page;
    private int size;
    private Map<String, Object> filters;
}
