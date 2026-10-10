package com.example.batchingwork.writer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.batchingwork.dto.ProcessingResultDto;
import com.example.batchingwork.entity.Employee;
import com.example.batchingwork.entity.EmployeeError;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeResultWriter implements ItemWriter<ProcessingResultDto> {

	private final JdbcBatchItemWriter<Employee> employeeJdbcWriter;
	private final JdbcBatchItemWriter<EmployeeError> errorJdbcWriter;
//	private final JdbcTemplate jdbcTemplate;

	@Override
	public void write(Chunk<? extends ProcessingResultDto> chunk) throws Exception {

		List<Employee> validEmployees = new ArrayList<>();
		List<EmployeeError> invalidEmployees = new ArrayList<>();

		/*
		 * Set<String> seenIds = new HashSet<>(); Set<String> seenEmails = new
		 * HashSet<>();
		 */
		/*
		 * for (ProcessingResultDto result : chunk.getItems()) {
		 * 
		 * if (result.getStatus() == ProcessingResultDto.Status.INVALID) {
		 * 
		 * invalidEmployees.add(result.getEmployeeError()); continue; }
		 * 
		 * Employee employee = result.getEmployee();
		 * 
		 * String employeeId = employee.getEmployeeId(); String email =
		 * employee.getEmail();
		 * 
		 * boolean duplicateId = !seenIds.add(employeeId); boolean duplicateEmail =
		 * !seenEmails.add(email);
		 * 
		 * if (duplicateId || duplicateEmail) {
		 * invalidEmployees.add(createDuplicateError(employee)); continue; }
		 * 
		 * Integer existingCount = jdbcTemplate.queryForObject(""" SELECT COUNT(*) FROM
		 * employee WHERE employee_id = ? OR email = ? """, Integer.class, employeeId,
		 * email);
		 * 
		 * if (existingCount != null && existingCount > 0) {
		 * invalidEmployees.add(createDuplicateError(employee)); continue; }
		 * 
		 * validEmployees.add(employee); }
		 */
		for (ProcessingResultDto result : chunk.getItems()) {
			if (result.getStatus() == ProcessingResultDto.Status.INVALID) {
				invalidEmployees.add(result.getEmployeeError());
			} else {
				validEmployees.add(result.getEmployee());
			}
		}

		if (!validEmployees.isEmpty()) {
			employeeJdbcWriter.write(new Chunk<>(validEmployees));
		}

		if (!invalidEmployees.isEmpty()) {
			errorJdbcWriter.write(new Chunk<>(invalidEmployees));
		}
	}
	/*
	 * private EmployeeError createDuplicateError(Employee employee) {
	 * 
	 * EmployeeError error = new EmployeeError();
	 * 
	 * error.setEmployeeId(employee.getEmployeeId());
	 * error.setName(employee.getName()); error.setEmail(employee.getEmail());
	 * error.setSalary(employee.getSalary().toPlainString());
	 * error.setErrorReason("Duplicate employee ID or email");
	 * 
	 * return error; }
	 */
}
