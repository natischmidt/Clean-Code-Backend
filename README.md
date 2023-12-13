Clean Code Project - Backend
Overview
Welcome to the Clean Code Project - Backend! This Java-based backend system is designed to handle booking functionalities for customers, employees, and admins. It seamlessly integrates with the corresponding frontend and implements Keycloak security for robust user authentication and authorization.

Features
User Management: Efficiently manage customers, employees, and admins.
Booking System: Streamlined functionality for handling bookings.
Keycloak Security: Robust authentication and authorization to ensure secure access.
Prerequisites
Before running the Clean Code backend, make sure you have the following installed:

Java Development Kit (JDK) 8 or later
Apache Maven
Keycloak server (for setting up authentication and authorization)
Setup
Clone the Repository:

bash
Copy code
git clone https://github.com/your-username/clean-code-backend.git
Build the Project:

bash
Copy code
cd clean-code-backend
mvn clean install
Configure Keycloak:

Set up a Keycloak server and configure realms, clients, and users.
Update the application.properties file with your Keycloak configurations.
Run the Application:

bash
Copy code
java -jar target/clean-code-backend.jar
Configuration
Update the application.properties file with your specific configurations, including:

Server Port: Port on which the backend server will run.
Database Connection: Configure the database connection details.
Keycloak Settings: Set up the Keycloak server details for authentication and authorization.
properties
Copy code
# application.properties

# Server Configurations
server.port=8080

# Database Configurations
spring.datasource.url=jdbc:mysql://localhost:3306/clean_code
spring.datasource.username=root
spring.datasource.password=password

# Keycloak Configurations
keycloak.auth-server-url=http://localhost:8081/auth
keycloak.realm=your-realm
keycloak.resource=your-client-id
keycloak.credentials.secret=your-client-secret
