# Users Service - Microservicio Spring Boot

![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.4-green)

Microservicio para gestión de usuarios con autenticación JWT y Spring Security.

## Requisitos

- Java 17
- Maven 3.8+
- MySQL 8.0+
## Configuración

1. Clona el repositorio:
   ```bash
   git clone https://github.com/Hinostrosaa/users-service.git

2. Configura la base de datos en application.properties:
    ```bash
    properties
   spring.application.name=users-service
   spring.datasource.url=jdbc:mysql://localhost:3306/users_service_db
   spring.datasource.username=root
   spring.datasource.password=
   spring.jpa.hibernate.ddl-auto=update
   spring.security.user.name=admin
   spring.security.user.password=admin
   jwt.secret=57420088103790198650538785975604

3. Ejecución
   ```bash
   mvn spring-boot:run
   El servicio estará disponible en: http://localhost:8080
