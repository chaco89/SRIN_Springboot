package com.example.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.service.EmployeeService;
import com.example.springboot.util.ListUtils;
import com.example.springboot.annotation.CreateMapping;
import com.example.springboot.annotation.DeleteMapping;
import com.example.springboot.annotation.UpdateMapping;
import com.example.springboot.annotation.ViewMapping;
import com.example.springboot.config.RedisConfig;
import com.example.springboot.constant.Path;
import com.example.springboot.dto.EmployeeDto;
import com.example.springboot.dto.ResourceDto;
import com.example.springboot.dto.ResourcesDto;

@Import({ RedisConfig.class })
@EnableCaching
@ImportAutoConfiguration(classes = { CacheAutoConfiguration.class, RedisAutoConfiguration.class })
@RestController
public class EmployeeController extends AbstractController{
	@Autowired
	private EmployeeService employeeService;

	@CreateMapping(path = Path.EMPLOYEE)
	public ResponseEntity<Void> index(@RequestBody EmployeeDto employeeDto) {
		employeeService.createEmployee(employeeDto);
		return sendCreateResponse(Path.EMPLOYEE);
	}

	@ViewMapping(path = Path.EMPLOYEE)
	public ResponseEntity<ResourcesDto> findEmployees() {
		List<EmployeeDto> employeeDtos = employeeService.findAllEmployee();
		List<ResourceDto> resourceDtoList = ListUtils.map(employeeDtos, ResourceDto::new);
		return sendReadListResponse(new ResourcesDto(resourceDtoList));
	}

	@ViewMapping(path = Path.EMPLOYEE_$ID)
	public ResponseEntity<ResourceDto> findEmployeeById(@PathVariable String id) {
		EmployeeDto employeeDto = employeeService.findEmployeeById(id);
		ResourceDto resourceDto = new ResourceDto(employeeDto);
		return sendReadResponse(resourceDto);
	}

	@UpdateMapping(path = Path.EMPLOYEE_$ID)
	public ResponseEntity<Void> findEmployeeById(@PathVariable String id, @RequestBody EmployeeDto employeeDto) {
		employeeDto.setId(id); // so the id wont be changed when sending the response body
		employeeService.updateEmployee(employeeDto);
		return sendUpdateResponse();
	}

	@DeleteMapping(path = Path.EMPLOYEE_$ID)
	public ResponseEntity<Void> deleteEmployeeById(@PathVariable String id) {
		employeeService.deleteEmployeeById(id);
		return sendDeleteResponse();
	}



	/* disable this to sync with node js assignment */
	/*
	@ViewMapping(path = Path.SEARCH_EMPLOYEE)
	public ResponseEntity<ResourcesDto> findEmployeeById(@RequestParam String name) {
		List<EmployeeDto> employeeDtos = employeeService.findEmployeeByName(name);
		List<ResourceDto> resourceDtoList = ListUtils.map(employeeDtos, ResourceDto::new);
	    return sendReadListResponse(new ResourcesDto(resourceDtoList));
    }
	 */

}
