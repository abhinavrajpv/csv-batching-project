package com.example.batchingwork.config;

import javax.sql.DataSource;

import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.batchingwork.entity.Employee;
import com.example.batchingwork.entity.EmployeeError;

@Configuration
public class EmployeeJdbcWriterConfig {

    @Bean
    public JdbcBatchItemWriter<Employee> employeeJdbcWriter(
            DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<Employee>()
                .dataSource(dataSource)
                .sql("""
                    INSERT INTO employee
                        (employee_id, name, email, salary)
                    VALUES
                        (:employeeId, :name, :email, :salary) ON CONFLICT DO NOTHING
                    """)
                .beanMapped()
                .assertUpdates(false)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<EmployeeError> employeeErrorJdbcWriter(
            DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<EmployeeError>()
                .dataSource(dataSource)
                .sql("""
                    INSERT INTO employee_error
                        (employee_id, name, email, salary, error_reason)
                    VALUES
                        (:employeeId, :name, :email, :salary, :errorReason)
                    """)
                .beanMapped()
                .build();
    }
}