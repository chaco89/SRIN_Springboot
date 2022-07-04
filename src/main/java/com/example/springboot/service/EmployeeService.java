package com.example.springboot.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.springboot.dto.EmployeeDto;

public interface EmployeeService{

	EmployeeDto createEmployee(EmployeeDto employeeDto);
	EmployeeDto updateEmployee(EmployeeDto employeeDto);

	List<EmployeeDto> findAllEmployee();
	EmployeeDto findEmployeeById(String id);

	List<EmployeeDto> findEmployeeByName(String name);
	void deleteEmployeeById(String id);
	void uploadFile(MultipartFile file);
	void deleteFile(String filename);


}
