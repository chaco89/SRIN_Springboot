package com.example.springboot.service;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

	final String EMPLOYEEE_LIST_CACHE = "EMP_CACHE";
	final String EMPLOYEEE_SINGLE_CACHE = "EMP_SINGLE_CACHE";

	@Autowired
	public EmployeeDao employeeDao;

	@Override
	@Cacheable(value = EMPLOYEEE_LIST_CACHE)
	public List<EmployeeDto> findAllEmployee() {
		List<Employee> list = employeeDao.findAll();
		List<EmployeeDto> employeeDtos = list.stream().map( emp -> {
			EmployeeDto dto = EmployeeUtil.toDto(emp);
			return dto;
		}).collect(Collectors.toList());

		return employeeDtos;
	}

	@Override
	@Cacheable(value = EMPLOYEEE_SINGLE_CACHE, key="#id")
	public EmployeeDto findEmployeeById(String id) {
		Optional<Employee> emp = employeeDao.findById(id);
		return emp.isPresent() ? EmployeeUtil.toDto(emp.get()) : null;
	}

	@Override
	@CacheEvict(value = EMPLOYEEE_LIST_CACHE, allEntries = true)
	@CachePut(value = EMPLOYEEE_SINGLE_CACHE, key="#result.id")
	public EmployeeDto createEmployee(EmployeeDto employeeDto) {
		Employee emp = employeeDao.save(EmployeeUtil.toEntity(employeeDto));
		employeeDto.setId(emp.getId());
		return employeeDto;
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = EMPLOYEEE_LIST_CACHE, allEntries = true)
	})
	@CachePut(value = EMPLOYEEE_SINGLE_CACHE, key="#root.args[0].id") // @CachePut(value = "employeeCacheId", key="#result.id")
	public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
		Optional<Employee> emp = employeeDao.findById(employeeDto.getId());

		if(emp.isPresent()) {
			Employee model = emp.get();
			EmployeeUtil.update(model, employeeDto);
			employeeDao.save(model);
		}

		return employeeDto;
	}

	@Override
	@Caching(evict = {
		@CacheEvict(value = EMPLOYEEE_LIST_CACHE, allEntries = true),
	    @CacheEvict(value = EMPLOYEEE_SINGLE_CACHE, key = "#id")
	})
	public void deleteEmployeeById(String id) {
		employeeDao.deleteById(id);
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
