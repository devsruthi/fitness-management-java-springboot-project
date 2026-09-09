package com.fitness.management.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Stage 3 only: proves Spring Boot reached the existing MySQL schema.
 * This class is read-only. It does not create, alter, or drop anything.
 */
@Component
public class DatabaseConnectionVerifier implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConnectionVerifier.class);

    private final DataSource dataSource;

    public DatabaseConnectionVerifier(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            String catalog = connection.getCatalog();
            List<String> tables = new ArrayList<>();

            try (ResultSet resultSet = statement.executeQuery("SHOW TABLES")) {
                while (resultSet.next()) {
                    tables.add(resultSet.getString(1));
                }
            }

            log.info("Connected to existing MySQL schema '{}' (read-only check).", catalog);
            log.info("Existing tables: {}", tables);
        }
    }
}
