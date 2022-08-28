package com.nashtech.rookies.AssetManagement.model.dto.respond;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nashtech.rookies.AssetManagement.model.entities.Accounts;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRespondDTO {

    private int accountId;

    private int informationId;

    private String firstName;

    private String lastName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateOfBirth;

    private String staffCode;

    private String locations;

    private String gender;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date joinedDate;

    private String userName;

    private String roleName;

    private String pass;
}
