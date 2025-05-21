# GestionPro - Enterprise Resource Management Platform

## Overview
GestionPro is a comprehensive enterprise resource management platform built with a microservices architecture, designed to provide a robust and scalable solution for enterprise resource management.

## Architecture
The application is built following a microservices architecture with the following components:

### Microservices
- **Auth Service**: Authentication and authorization management
- **Employee Service**: Employee management system
- **Product Service**: Product management system
- **Notification Service**: Notification system
- **API Gateway**: Single entry point for all services
- **Eureka Server**: Service registry and discovery

### Infrastructure
- **Databases**: PostgreSQL (separate instances for each service)
- **Containers**: Docker
- **Orchestration**: Docker Compose
- **Configuration**: Spring Cloud Config Server

## Core Technologies
- Java 21
- Spring Boot 3.3.4
- Spring Cloud 2023.0.3
- Spring Security
- Spring Data JPA
- PostgreSQL 13
- Docker & Docker Compose
- JWT Authentication
- Spring Cloud Gateway
- Eureka Server

## Prerequisites
- Java 21 or higher
- Maven 3.6 or higher
- Docker and Docker Compose
- Git

## Installation and Deployment

### 1. Clone the Repository
```bash
git clone [REPOSITORY_URL]
cd gestionpro
```

### 2. Environment Configuration
Ensure the following environment variables are configured:
- Database variables (configured in docker-compose.yml)
- Security configurations
- Service configurations

### 3. Docker Compose Deployment
```bash
docker-compose up -d
```

### 4. Service Verification
- API Gateway: http://localhost:8080
- Eureka Dashboard: http://localhost:8761
- Notification Service: http://localhost:8086

## Project Structure
```
gestionpro/
├── apigateway/          # API Gateway Service
├── authservice/         # Authentication Service
├── employeeservice/     # Employee Management Service
├── productservice/      # Product Management Service
├── notificationservice/ # Notification Service
├── eurekaserver/       # Eureka Server
└── docker-compose.yml  # Docker Compose Configuration
```

## Security
- JWT-based authentication
- Spring Security for endpoint protection
- Service-specific database instances
- Secure credentials and environment variables

## API Documentation
API documentation is available through Swagger UI at:
- API Gateway: http://localhost:8080/swagger-ui.html

## Testing
To execute the test suite:
```bash
mvn test
```

## Continuous Integration/Continuous Deployment
The project includes configuration for continuous integration and continuous deployment pipelines.

## Contributing
1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License
This project is licensed under the MIT License - see the `LICENSE` file for details.

## Support
For support, please contact the development team or open an issue in the repository.

## Acknowledgments
- Spring Boot Team
- Docker Community
- PostgreSQL Team
- All project contributors 
