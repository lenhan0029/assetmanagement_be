package com.nashtech.rookies.AssetManagement.model.dto.request;

import com.nashtech.rookies.AssetManagement.model.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {

    @NotEmpty(message = "full name is required")
    private String fullName;

    @NotEmpty(message = "date of birth is required")
    private String dateOfBirth;

    @NotEmpty(message = "role is required")
    private Role role;
}
