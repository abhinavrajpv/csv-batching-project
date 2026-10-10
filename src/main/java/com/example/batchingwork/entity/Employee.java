package com.example.batchingwork.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "emp_id",
            columnNames = "employee_id"),
        @UniqueConstraint(
            name = "emp_email",
            columnNames = "email")
    }
)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, unique=true)
    private String employeeId;

    private String name;

    @Column(name = "email", nullable = false, unique=true)
    private String email;

    private BigDecimal salary;
}
