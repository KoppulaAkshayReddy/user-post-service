package com.akshay.rest.webservices.userpost.entity;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Details about the user.")
public class UserVersion2 {
	
	private int id;
	
	@Size(min=5, message="Full name should have atleast 5 characters")
	@ApiModelProperty(notes="Full name should have atleast 5 characters")
	private String fullName;
	
	@Email
	private String email;
	
	@JsonIgnore
	private String password;
	
	@Past
	@ApiModelProperty(notes="Birth date should be in the past")
	private Date birthDate;

	public UserVersion2(int id, String fullName, String email, String password, Date birthDate) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.birthDate = birthDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
}
