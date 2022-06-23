package com.example.springboot.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.springboot.model.Employee;


public interface EmployeeDao extends MongoRepository<Employee, String> {
    
	@Query("{'name' : {$regex: ?0 }})")
    List<Employee> findByName(String name); // this is not used anymore as i disable the endpoint 
    
	@Query("{ id : ?0 }")
	Employee findEmployeeById(Long id);  // it's not used anymore as i use findbyid from mongorepository
}