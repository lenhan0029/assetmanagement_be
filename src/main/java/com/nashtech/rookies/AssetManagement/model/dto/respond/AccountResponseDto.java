package com.nashtech.rookies.AssetManagement.model.dto.respond;

import com.nashtech.rookies.AssetManagement.model.dto.AccountDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Date;

@Getter
@Setter
public class AccountResponseDto {


	@NotEmpty(message = "First name must not be empty")
	private String firstName;
	@NotEmpty(message = "Last name must not be empty")
	private String lastName;
	
	@NotEmpty(message = "Staff code must not be empty")
	private String staffCode;
	
	private Integer accountId;
	
	private Integer roleId;
//	
	private String roleName;
	

	public AccountResponseDto(){}

	public AccountResponseDto(String firstName, String lastName, String staffCode,Integer accountId, Integer roleId,String roleName) {

		this.firstName = firstName;
		this.lastName = lastName;
		this.staffCode = staffCode;
		this.accountId=accountId;
		this.roleId=roleId;
		this.roleName=roleName;
	}
}
