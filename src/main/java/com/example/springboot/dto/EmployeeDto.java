package com.example.springboot.dto;

import java.time.LocalDate;

import com.example.springboot.annotation.Json;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.example.springboot.constant.JsonKey;

@Json
public class EmployeeDto {

	@Expose
	@SerializedName(JsonKey.ID)
	private String id;
	
	@Expose
    @SerializedName(JsonKey.NAME)
	private String name;

	@Expose
    @SerializedName(JsonKey.GENDER)
	private String gender;
	
	@Expose
    @SerializedName(JsonKey.BIRTH_DATE)
    private LocalDate birthDate;
	
	@Expose
    @SerializedName(JsonKey.HIRE_DATE)
    private LocalDate hireDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getHireDate() {
		return hireDate;
	}

	public void setHireDate(LocalDate hireDate) {
		this.hireDate = hireDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
}
