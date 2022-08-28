package com.nashtech.rookies.AssetManagement.model.dto;

import com.nashtech.rookies.AssetManagement.model.dto.InformationDto;
import com.nashtech.rookies.AssetManagement.model.dto.RoleDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AccountDto {

//	@Pattern(regexp = "\\d*", message = "Account id must be a number")
	private Integer accountId;
//	@NotEmpty(message = "Username must not be empty")
//	private String username;
//	@NotEmpty(message = "Password must not be empty")
//	private String password;
//	private boolean status;
//	private boolean firtLogin;
//	@NotEmpty(message = "Password must not be empty")
	private RoleDto role;

//	@NotEmpty(message = "Information must not be empty")
//	private InformationDto information;

	public AccountDto(){}

	public AccountDto(Integer accountId /*, String username, String password, boolean status, boolean firtLogin*/, RoleDto role/*, InformationDto information*/) {
		this.accountId = accountId;
//		this.username = username;
//		this.password = password;
//		this.status = status;
//		this.firtLogin = firtLogin;
		this.role = role;
//		this.information = information;
	}
}
