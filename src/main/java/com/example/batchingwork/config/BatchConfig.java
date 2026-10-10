package com.example.batchingwork.config;

import java.util.concurrent.Future;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batchingwork.dto.EmployeeCsvDto;
import com.example.batchingwork.dto.ProcessingResultDto;

@Configuration
public class BatchConfig {

	@Bean
	public Step fileValidationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			EmployeeFileValidationTasklet tasklet) {

		return new StepBuilder("fileValidationStep", jobRepository).tasklet(tasklet, transactionManager)
				.allowStartIfComplete(true).build();
	}

	@Bean
	public Step employeeStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			FlatFileItemReader<EmployeeCsvDto> employeeReader,
			AsyncItemProcessor<EmployeeCsvDto, ProcessingResultDto> asyncEmployeeProcessor,
			AsyncItemWriter<ProcessingResultDto> asyncEmployeeWriter) {

		return new StepBuilder("employeeStep", jobRepository)
				.<EmployeeCsvDto, Future<ProcessingResultDto>>chunk(10, transactionManager).reader(employeeReader)
				.processor(asyncEmployeeProcessor).writer(asyncEmployeeWriter).build();
	}

	@Bean
	public Job employeeJob(JobRepository jobRepository, Step fileValidationStep, Step employeeStep) {

		return new JobBuilder("employeeJob", jobRepository).start(fileValidationStep).next(employeeStep).build();
	}
}