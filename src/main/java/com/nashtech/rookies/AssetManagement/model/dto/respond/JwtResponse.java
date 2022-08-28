package com.nashtech.rookies.AssetManagement.model.dto.respond;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private int id;
	private String username;
	private String roles;

	private boolean first_login;

	public JwtResponse(String token, int id, String username, String roles, boolean first_login) {
		this.token = token;
		this.type = type;
		this.id = id;
		this.username = username;
		this.roles = roles;
		this.first_login = first_login;
	}
}