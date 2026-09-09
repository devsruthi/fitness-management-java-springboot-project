
# Database setup and connection

These SQL files create the MySQL database used by the Spring Boot API:

- schema
- tables
- stored procedures
- sample data

Run them in this order in MySQL Workbench:

1. `01_create_schema.sql`
2. `02_create_tables.sql`
3. `03_stored_procedures.sql`
4. `04_seed_data.sql`

Then set your local MySQL details in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/health_club_operations_system
spring.datasource.username=
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=none
```

The database name in the URL must match your MySQL database. Keep `ddl-auto=none` so Hibernate does not change the tables.

# Run application

1. Start MySQL.
2. In the project folder, run:

```bash
./mvnw spring-boot:run
```

Or open `FitnessManagementApplication.java` and click **Run**.

The API is ready when you see `Started FitnessManagementApplication`.

Base URL: `http://localhost:8080`

To stop the app, press `Ctrl + C` in the terminal.

If the app fails on startup, MySQL is not running, or the database name / username / password is wrong.


# Testing

Use a browser or Postman:

- `GET http://localhost:8080/api/subscription-plans`
- `GET http://localhost:8080/api/sessions`

