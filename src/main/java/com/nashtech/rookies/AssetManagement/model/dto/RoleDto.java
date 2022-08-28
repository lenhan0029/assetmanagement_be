package com.nashtech.rookies.AssetManagement.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class RoleDto {
	@Pattern(regexp = "\\d*", message = "Role id must be a number")
	private Integer roleId;
	@NotEmpty(message = "Role name must not be empty")
	private String roleName;

	public RoleDto() {}

	public RoleDto(Integer roleId, String roleName) {
		this.roleId = roleId;
		this.roleName = roleName;
	}
}
