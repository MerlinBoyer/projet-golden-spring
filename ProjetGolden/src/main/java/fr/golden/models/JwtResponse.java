package fr.golden.models;

import java.io.Serializable;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtResponse implements Serializable {
	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	private final String role;

	
	public JwtResponse(String jwttoken) {
		this.jwttoken = jwttoken;
		this.role = "ROLE_UNKNOWN";
	}
	
	public JwtResponse(String jwttoken, SimpleGrantedAuthority authority) {
		this.jwttoken = jwttoken;
		this.role = authority != null ? authority.getAuthority() : "ROLE_UNKNOWN";
	}

	public String getToken() {
		return this.jwttoken;
	}
	
	public String getRole() {
		return this.role;
	}
}