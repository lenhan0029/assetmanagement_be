package com.nashtech.rookies.AssetManagement.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    @NotEmpty(message = "First name must not be empty")
    @Length(max = 128, message = "first name maximum is 128 characters")
    private String firstName;

    @NotEmpty(message = "Last name must not be empty")
    @Length(max = 128, message = "last name maximum is 128 characters")
    private String lastName;

    @JsonProperty("birth")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    @NotEmpty(message = "location must not be empty")
    private String locations;

    @NotEmpty(message = "Gender must not be empty")
    private String gender;

    @JsonProperty("join")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date joinDate;

    @NotEmpty(message = "role must not be empty")
    private String roleName;

    @NotEmpty(message = "prefix must not be empty")
    private String prefix;


}
