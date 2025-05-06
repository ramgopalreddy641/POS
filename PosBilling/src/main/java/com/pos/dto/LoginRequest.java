package com.pos.dto;

import com.pos.model.Role;

public class LoginRequest {
    private String username;
    private String password;
    public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	private Role role;
  
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
  
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
