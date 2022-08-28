package com.nashtech.rookies.AssetManagement.model.dto.respond;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailDto {

    private int accountId;

    private String staffCode;

    private String firstName;

    private String lastName;

    private String userName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    private String gender;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date joinedDate;

    private String roleName;

    private String locations;



}
