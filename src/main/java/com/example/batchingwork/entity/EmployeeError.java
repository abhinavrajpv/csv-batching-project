package com.example.batchingwork.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_error")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeError {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "error_id")
	private Long errorId;

	@Column(name = "employee_id")
	private String employeeId;

	private String name;

	private String email;

	private String salary;

	@Column(name = "error_reason", length = 2000)
	private String errorReason;
}