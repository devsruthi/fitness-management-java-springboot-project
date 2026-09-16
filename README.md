# Health Club Operations REST API

A Java Spring Boot application that provides REST services for a fitness and wellness centre.

This project was developed for the M604 Advanced Programming module.

# Database setup and connection

Run the SQL files in this order in MySQL Workbench:

1. `sql/01_create_schema.sql`
2. `sql/02_create_tables.sql`
3. `sql/03_seed_data.sql`

Then set your local MySQL details in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/health_club_operations_system
spring.datasource.username=root
spring.datasource.password=root
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

Use a browser or Postman.

A ready-to-import Postman collection is in `docs/Health-Club-API.postman_collection.json`.

1. Start the application (MySQL + `./mvnw spring-boot:run`).
2. Open Postman → **Import** → select that file.
3. Confirm `baseUrl` is `http://localhost:8080`, then send requests.

Quick checks:

- `GET http://localhost:8080/api/subscription-plans`
- `GET http://localhost:8080/api/service-types`
- `GET http://localhost:8080/api/members`
- `GET http://localhost:8080/api/sessions`

A successful response returns JSON.
