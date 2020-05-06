package com.employee.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.exception.RecordNotFoundException;
import com.employee.model.Employee;
import com.employee.service.EmployeeService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	EmployeeService empservice;
	
	private static final Logger logger=LoggerFactory.getLogger(EmployeeController.class);
	
	
	@GetMapping
	public ResponseEntity<List<Employee>> findAllemployees() throws RecordNotFoundException{
		List<Employee> emplist=empservice.findByAllemp();
		logger.debug("emplist size: "+emplist.size());
		return new ResponseEntity<List<Employee>>(emplist, new HttpHeaders(), HttpStatus.OK);
	}
	
	
}
