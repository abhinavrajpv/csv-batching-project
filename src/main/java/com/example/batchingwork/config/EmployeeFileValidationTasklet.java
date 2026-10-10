package com.example.batchingwork.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFileValidationTasklet implements Tasklet {

    @Value("${employee.csv.path}")
    private String filePath;

    @Value("${employee.csv.max-size}")
    private long maxSize;

    @Value("${employee.csv.headers}")
    private String expectedHeaders;

    @Override
    public RepeatStatus execute(
            StepContribution contribution,
            ChunkContext chunkContext) throws Exception {

        Path path = Path.of(filePath);
        List<String> errors = new ArrayList<>();

        if (!Files.isRegularFile(path)
                || !Files.isReadable(path)) {
            throw new IllegalArgumentException(
                    "CSV file does not exist or is not readable");
        }

        if (!path.getFileName().toString()
                .toLowerCase(Locale.ROOT).endsWith(".csv")) {
            errors.add("Only CSV files are allowed");
        }

        long fileSize = Files.size(path);

        if (fileSize == 0) {
            errors.add("CSV file is empty");
        } else if (fileSize > maxSize) {
            errors.add("CSV file exceeds maximum allowed size");
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException(
                    String.join("; ", errors));
        }

        List<String> requiredHeaders =
                Arrays.stream(expectedHeaders.split(",", -1))
                        .map(String::trim)
                        .toList();

        try (BufferedReader reader =
                     Files.newBufferedReader(
                             path, StandardCharsets.UTF_8)) {

            String headerLine = reader.readLine();

            if (headerLine == null || headerLine.isBlank()) {
                throw new IllegalArgumentException(
                        "CSV header is missing");
            }


            List<String> actualHeaders =
                    Arrays.stream(headerLine.split(",", -1))
                            .map(String::trim)
                            .toList();

            if (!actualHeaders.equals(requiredHeaders)) {
                throw new IllegalArgumentException(
                        "Invalid CSV headers. Expected: "
                                + requiredHeaders
                                + ", found: " + actualHeaders);
            }

            String line;
            int lineNumber = 1;
            int dataRows = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (line.isBlank()) {
                    throw new IllegalArgumentException(
                            "Empty row at line " + lineNumber);
                }

                if (line.split(",", -1).length
                        != requiredHeaders.size()) {
                    throw new IllegalArgumentException(
                            "Incorrect column count at line "
                                    + lineNumber);
                }

                dataRows++;
            }

            if (dataRows == 0) {
                throw new IllegalArgumentException(
                        "CSV contains no data rows");
            }

        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Unable to read CSV file", ex);
        }

        System.out.println("CSV file validation successful");

        return RepeatStatus.FINISHED;
    }
}