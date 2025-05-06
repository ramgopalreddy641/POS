package com.pos.dto;

import com.pos.model.EmployeeRole;

public class EmployeeLoginRequest {
		    private String username;
		    private String password;
		    public EmployeeRole getRole() {
				return role;
			}
			public void setRole(EmployeeRole role) {
				this.role = role;
			}
			private EmployeeRole role;
		  
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