package com.example.springboot.service;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import com.example.springboot.config.RedisConfig;
import com.example.springboot.dao.EmployeeDao;
import com.example.springboot.dto.EmployeeDto;
import com.example.springboot.model.Employee;
import com.example.springboot.util.EmployeeUtil;

@Import({ RedisConfig.class })
@EnableCaching
/*
@ImportAutoConfiguration(classes = {
		CacheAutoConfiguration.class,
		RedisAutoConfiguration.class
})*/
@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	public EmployeeDao employeeDao;

	@Override
	@CacheEvict(value = {"employeeCache", "employeeCacheId"}, allEntries = true)
	public void createEmployee(EmployeeDto employeeDto) {
		employeeDao.save(EmployeeUtil.toEntity(employeeDto));
	}

	@Override
	//@CacheEvict(value = "first", key = "#cacheKey")
	@CacheEvict(value = {"employeeCache", "employeeCacheId"}, allEntries = true)
	public void updateEmployee(EmployeeDto employeeDto) {
		Optional<Employee> emp = employeeDao.findById(employeeDto.getId());

		if(emp.isPresent()) {
			Employee model = emp.get();
			EmployeeUtil.update(model, employeeDto);
			employeeDao.save(model);
		}
	}

	@Override
	@CacheEvict(value = {"employeeCache", "employeeCacheId"}, allEntries = true)
	public void deleteEmployeeById(String id) {
		employeeDao.deleteById(id);
	}



	@Override
	@Cacheable(value = "employeeCache")
	public List<EmployeeDto> findAllEmployee() {
		List<Employee> list = employeeDao.findAll();
		List<EmployeeDto> employeeDtos = list.stream().map( emp -> {
			EmployeeDto dto = EmployeeUtil.toDto(emp);
			return dto;
		}).collect(Collectors.toList());

		return employeeDtos;
	}

	@Override
	@Cacheable(value = "employeeCacheId")
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
