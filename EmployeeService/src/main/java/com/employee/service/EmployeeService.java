package com.employee.service;

import java.util.List;

import com.employee.exception.RecordNotFoundException;
import com.employee.model.Employee;

public interface EmployeeService {

	public List<Employee> findByAllemp() throws RecordNotFoundException;

}
