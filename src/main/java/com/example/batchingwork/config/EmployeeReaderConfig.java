package com.example.batchingwork.config;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.example.batchingwork.dto.EmployeeCsvDto;

@Configuration
public class EmployeeReaderConfig {

	@Bean
	@StepScope
	public FlatFileItemReader<EmployeeCsvDto> employeeReader(@Value("${employee.csv.path}") String filePath) {

		return new FlatFileItemReaderBuilder<EmployeeCsvDto>().name("employeeReader")
				.resource(new FileSystemResource(filePath)).linesToSkip(1).delimited().delimiter(",")
				.names("employeeId", "name", "email", "salary").targetType(EmployeeCsvDto.class).build();
	}
}