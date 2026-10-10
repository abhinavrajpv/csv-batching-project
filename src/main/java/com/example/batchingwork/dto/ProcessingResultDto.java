package com.example.batchingwork.dto;

import com.example.batchingwork.entity.Employee;
import com.example.batchingwork.entity.EmployeeError;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProcessingResultDto {

    public enum Status {
        VALID,
        INVALID
    }

    private final Status status;
    private final Employee employee;
    private final EmployeeError employeeError;

    public static ProcessingResultDto valid(Employee employee) {
        return new ProcessingResultDto(
                Status.VALID, employee, null);
    }

    public static ProcessingResultDto invalid(
            EmployeeError employeeError) {
        return new ProcessingResultDto(
                Status.INVALID, null, employeeError);
    }
}