package com.nashtech.rookies.AssetManagement.model.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ChangePasswordDTO {
    private String old_password;

    @NotEmpty(message = "New password must not be empty")
    private String new_password;

    public ChangePasswordDTO(){}

    public ChangePasswordDTO(String old_password, String new_password) {
        this.old_password = old_password;
        this.new_password = new_password;
    }
}
