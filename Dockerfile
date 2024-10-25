# Dockerfile

# Usa una imagen base de Maven para construir el proyecto
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app

# Copia el archivo pom.xml y las dependencias del proyecto
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia todo el proyecto y compila la aplicación
COPY . .
RUN mvn clean package -DskipTests

# Usa una imagen de JDK para ejecutar la aplicación
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copia el archivo JAR generado desde la fase de construcción
COPY --from=build /app/target/adm-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto donde la aplicación va a estar escuchando
EXPOSE 8080

# Definir la entrada para ejecutar el JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
