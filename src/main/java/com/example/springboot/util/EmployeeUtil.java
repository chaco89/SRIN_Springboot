package com.example.springboot.util;

import com.example.springboot.dto.EmployeeDto;
import com.example.springboot.model.Employee;

public final class EmployeeUtil {
	
    public static EmployeeDto toDto(Employee emp) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(emp.getId());
        dto.setGender(emp.getGender());
        dto.setBirthDate(emp.getBirthDate());
        dto.setHireDate(emp.getHireDate());
        dto.setName(emp.getName());
        return dto;
    }

    public static Employee toEntity(EmployeeDto dto) {
    	Employee emp = new Employee();
    	emp.setName(dto.getName());
    	emp.setGender(dto.getGender());
    	emp.setBirthDate(dto.getBirthDate());
    	emp.setHireDate(dto.getHireDate());
    	return emp;
    }
    
    public static void update(Employee model, EmployeeDto dto) {
    	model.setBirthDate(dto.getBirthDate());
    	model.setName(dto.getName());
    	model.setHireDate(dto.getHireDate());
    	model.setGender(dto.getGender());
    }
    
}
