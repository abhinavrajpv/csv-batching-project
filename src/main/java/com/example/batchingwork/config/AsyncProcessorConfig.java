package com.example.batchingwork.config;

import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import com.example.batchingwork.dto.EmployeeCsvDto;
import com.example.batchingwork.dto.ProcessingResultDto;
import com.example.batchingwork.processor.EmployeeItemProcessor;
import com.example.batchingwork.writer.EmployeeResultWriter;

@Configuration
public class AsyncProcessorConfig {

    @Bean
    public AsyncItemProcessor<EmployeeCsvDto, ProcessingResultDto>
    asyncEmployeeProcessor(
            EmployeeItemProcessor delegate,
            TaskExecutor processorTaskExecutor) {

        AsyncItemProcessor<EmployeeCsvDto, ProcessingResultDto> processor =
                new AsyncItemProcessor<>(delegate);

        processor.setTaskExecutor(processorTaskExecutor);

        return processor;
    }

    @Bean
    public AsyncItemWriter<ProcessingResultDto> asyncEmployeeWriter(
            EmployeeResultWriter delegate) {

        return new AsyncItemWriter<>(delegate);
    }
}