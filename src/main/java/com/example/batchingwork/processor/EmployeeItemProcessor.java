package com.example.batchingwork.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.batchingwork.dto.EmployeeCsvDto;
import com.example.batchingwork.dto.ProcessingResultDto;
import com.example.batchingwork.entity.Employee;
import com.example.batchingwork.entity.EmployeeError;

@Component
public class EmployeeItemProcessor implements ItemProcessor<EmployeeCsvDto, ProcessingResultDto> {

	@Override
	public ProcessingResultDto process(EmployeeCsvDto record) {		
		/*
		 * if ("1016".equals(record.getEmployeeId())) { throw new
		 * RuntimeException("TEST: Processor failure"); }
		 * 
		 */
		 
		List<String> errors = new ArrayList<>();
		BigDecimal salary = null;

		if (record.getEmployeeId() == null || record.getEmployeeId().isBlank()) {
			errors.add("Employee ID is missing");
		} else {
			try {
				Long.parseLong(record.getEmployeeId());
			} catch (NumberFormatException ex) {
				errors.add("Employee ID must be numeric");
			}
		}

		if (record.getName() == null || record.getName().isBlank()) {
			errors.add("Name is missing");
		}

		if (record.getEmail() == null || !record.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			errors.add("Invalid email");
		}

		if (record.getSalary() == null || record.getSalary().isBlank()) {
			errors.add("Salary is missing");
		} else {
			try {
				salary = new BigDecimal(record.getSalary());

				if (salary.signum() <= 0) {
					errors.add("Salary must be positive");
				}
			} catch (NumberFormatException ex) {
				errors.add("Salary must be numeric");
			}
		}

		if (!errors.isEmpty()) {
			EmployeeError error = new EmployeeError();

			error.setEmployeeId(record.getEmployeeId());
			error.setName(record.getName());
			error.setEmail(record.getEmail());
			error.setSalary(record.getSalary());
			error.setErrorReason(String.join("; ", errors));

			return ProcessingResultDto.invalid(error);
		}

		Employee employee = new Employee();

		employee.setEmployeeId(record.getEmployeeId());
		employee.setName(record.getName());
		employee.setEmail(record.getEmail());
		employee.setSalary(salary);

		return ProcessingResultDto.valid(employee);
	}
}