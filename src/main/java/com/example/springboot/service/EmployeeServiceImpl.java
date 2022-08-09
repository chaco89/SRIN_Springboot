package com.example.springboot.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.integration.sftp.session.SftpFileInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.springboot.config.RedisConfig;
import com.example.springboot.config.SftpConfig.UploadGateway;
import com.example.springboot.constant.CacheKey;
import com.example.springboot.dao.EmployeeDao;
import com.example.springboot.dto.EmployeeDto;
import com.example.springboot.model.Employee;
import com.example.springboot.util.EmployeeUtil;

@Import({ RedisConfig.class })
@EnableCaching
@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	public UploadGateway uploadGateWay;

	@Autowired
	public EmployeeDao employeeDao;

	@Override
	@Cacheable(value = CacheKey.EMPLOYEEE_LIST_CACHE)
	public List<EmployeeDto> findAllEmployee() {
		List<Employee> list = employeeDao.findAll();
		List<EmployeeDto> employeeDtos = list.stream().map( emp -> {
			EmployeeDto dto = EmployeeUtil.toDto(emp);
			return dto;
		}).collect(Collectors.toList());

		return employeeDtos;
	}

	@Override
	@Cacheable(value = CacheKey.EMPLOYEEE_SINGLE_CACHE, key="#id")
	public EmployeeDto findEmployeeById(String id) {
		Optional<Employee> emp = employeeDao.findById(id);
		return emp.isPresent() ? EmployeeUtil.toDto(emp.get()) : null;
	}

	@Override
	@CacheEvict(value = CacheKey.EMPLOYEEE_LIST_CACHE, allEntries = true)
	@CachePut(value = CacheKey.EMPLOYEEE_SINGLE_CACHE, key="#result.id")
	public EmployeeDto createEmployee(EmployeeDto employeeDto) {
		Employee emp = employeeDao.save(EmployeeUtil.toEntity(employeeDto));
		employeeDto.setId(emp.getId());
		return employeeDto;
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = CacheKey.EMPLOYEEE_LIST_CACHE, allEntries = true)
	})
	@CachePut(value = CacheKey.EMPLOYEEE_SINGLE_CACHE, key="#root.args[0].id") // @CachePut(value = "employeeCacheId", key="#result.id")
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
		@CacheEvict(value = CacheKey.EMPLOYEEE_LIST_CACHE, allEntries = true),
	    @CacheEvict(value = CacheKey.EMPLOYEEE_SINGLE_CACHE, key = "#id")
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

	@Value("${sftp.source.directory:/}")
	public String sftpSourceDir;

	@Override
	public void uploadFile(MultipartFile file) {
		try {

			Function<MultipartFile, String> extractFileName = (f) -> {
				String originalName = f.getOriginalFilename();
				String name = originalName.substring(0, originalName.indexOf("."));
				String ext = originalName.substring(originalName.indexOf("."), originalName.length());
				return name + ext;
			};

			String fileName = extractFileName.apply(file);
			File newFile = new File(sftpSourceDir + fileName);
        	try (OutputStream os = new FileOutputStream(newFile)) {
        	    os.write(file.getBytes());
        	}

			uploadGateWay.upload(newFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteFile(String filename) {
		List<SftpFileInfo> fileInfos = uploadGateWay.list();
		for(SftpFileInfo f : fileInfos) {
			uploadGateWay.delete(f.getFilename());
		}
	}

}
