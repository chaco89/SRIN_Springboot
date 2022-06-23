package com.example.springboot.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("employee")
public class Employee {

    @Id
    private String id;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private LocalDate hireDate;
    
    public Employee() {
    	
    }
    
    public Employee(String id, String name, String gender, LocalDate birthDate, LocalDate hireDate) {
        super();
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
    }

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	
}
