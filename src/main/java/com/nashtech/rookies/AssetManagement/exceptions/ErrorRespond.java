package com.nashtech.rookies.AssetManagement.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRespond {


    private int statusCode;

    private String message;

    private Map<String, String> validateMessage;

    public ErrorRespond(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
