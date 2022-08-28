package com.nashtech.rookies.AssetManagement.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nashtech.rookies.AssetManagement.model.dto.AccountDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@Setter
public class InformationDto {



	private int informationId;
	private String userName;
	private int accountId;
	@NotEmpty(message = "First name must not be empty")
	private String firstName;
	@NotEmpty(message = "Last name must not be empty")
	private String lastName;
//	@NotEmpty(message = "Date of birth must not be empty")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date dateOfBirth;
	@NotEmpty(message = "Staff code must not be empty")
	private String staffCode;
	@NotEmpty(message = "Location must not be empty")
	private String locations;

	@NotEmpty(message = "Gender must not be empty")
	private String gender;

//	@NotEmpty(message = "joinDate must not be empty")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date joinedDate;
//
//	@NotEmpty(message = "roleId must not be empty")
	private Integer roleId;
//	
	private String roleName;
	

	public InformationDto(){}

	public InformationDto(String firstName, String lastName, Date dateOfBirth, String staffCode, String locations, String gender, Date joinDate , Integer roleId,String roleName) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.staffCode = staffCode;
		this.locations = locations;
		this.gender = gender;
		this.joinedDate = joinDate;
		this.roleId=roleId;
		this.roleName=roleName;
	}
}
