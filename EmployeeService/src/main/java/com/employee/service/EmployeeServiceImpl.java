package com.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.employee.exception.RecordNotFoundException;
import com.employee.model.Employee;
import com.employee.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepository empRepository;
	
	@Transactional
	@Override
	public List<Employee> findByAllemp() throws RecordNotFoundException {
		List<Employee> emplist=empRepository.findAll();
		if(emplist.size()==0) {
			throw new RecordNotFoundException("No employee Records");
		}
		return emplist;
	}

	
	
	
	
}
