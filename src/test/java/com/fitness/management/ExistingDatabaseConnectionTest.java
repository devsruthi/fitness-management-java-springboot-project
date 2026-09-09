package com.fitness.management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExistingDatabaseConnectionTest {

    private static final Set<String> EXPECTED_TABLES = Set.of(
            "Members",
            "Trainers",
            "Subscription_Plans",
            "Member_Subscriptions",
            "Payments",
            "Service_Types",
            "Sessions",
            "Bookings"
    );

    @Autowired
    private DataSource dataSource;

    @Test
    void connectsToExistingFitnessSchemaWithoutCreatingTables() throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            assertEquals("health_club_operations_system", connection.getCatalog());

            Set<String> actualTables = new HashSet<>();
            try (ResultSet resultSet = statement.executeQuery("SHOW TABLES")) {
                while (resultSet.next()) {
                    actualTables.add(resultSet.getString(1));
                }
            }

            assertTrue(actualTables.containsAll(EXPECTED_TABLES),
                    "Existing schema is missing expected tables: " + actualTables);
        }
    }
}
