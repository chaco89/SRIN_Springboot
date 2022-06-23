package com.example.springboot.service;

import java.util.List;

import com.example.springboot.dto.EmployeeDto;

public interface EmployeeService{

	void createEmployee(EmployeeDto employeeDto);
	void updateEmployee(EmployeeDto employeeDto);
	
	List<EmployeeDto> findAllEmployee();
	EmployeeDto findEmployeeById(String id);

	List<EmployeeDto> findEmployeeByName(String name);
	void deleteEmployeeById(String id);
	
	
}
