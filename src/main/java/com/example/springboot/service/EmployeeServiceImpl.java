package com.example.springboot.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dao.EmployeeDao;
import com.example.springboot.dto.EmployeeDto;
import com.example.springboot.model.Employee;
import com.example.springboot.util.EmployeeUtil;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	public EmployeeDao employeeDao;
	
	@Override
	public void createEmployee(EmployeeDto employeeDto) {
		employeeDao.save(EmployeeUtil.toEntity(employeeDto));
	}
	
	@Override
	public void updateEmployee(EmployeeDto employeeDto) {
		 Optional<Employee> emp = employeeDao.findById(employeeDto.getId());
         
		 Employee model = emp.isPresent() ? emp.get() : null;
		 if(model != null) {
			 EmployeeUtil.update(model, employeeDto);
		 }
		
		 employeeDao.save(model);
	}
	
	@Override
	public void deleteEmployeeById(String id) {
		 employeeDao.deleteById(id);
	}
	
	
	@Override
	public List<EmployeeDto> findAllEmployee() {
         List<Employee> list = employeeDao.findAll();      
         List<EmployeeDto> employeeDtos = list.stream().map( emp -> { 
        	 EmployeeDto dto = EmployeeUtil.toDto(emp); 
        	 return dto; 
         }).collect(Collectors.toList());
         
         return employeeDtos;
    }
	
	@Override
	public EmployeeDto findEmployeeById(String id) {
         Optional<Employee> emp = employeeDao.findById(id);
         return emp.isPresent() ? EmployeeUtil.toDto(emp.get()) : null;
    }
	
	@Override
	public List<EmployeeDto> findEmployeeByName(String name) {
		 List<Employee> list = employeeDao.findByName(name);
		 List<EmployeeDto> employeeDtos = list.stream().map( emp -> { 
        	 EmployeeDto dto = EmployeeUtil.toDto(emp); 
        	 return dto; 
         }).collect(Collectors.toList());
         
         return employeeDtos;
    }
	
	
	
}
