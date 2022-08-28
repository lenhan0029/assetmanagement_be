package com.nashtech.rookies.AssetManagement.model.dto.respond;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

    private String message;
    private Integer statuscode;

    public MessageResponse(String message) {
        this.message = message;
    }



    public MessageResponse(String message, Integer statuscode) {
        this.message = message;
        this.statuscode = statuscode;
    }

}
