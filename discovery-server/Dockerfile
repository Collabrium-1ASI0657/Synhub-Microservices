# ================================
# 🧱 STAGE 1 - Build Stage
# ================================
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos el pom.xml y descargamos dependencias antes para aprovechar la caché de Docker
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente
COPY src ./src

# Compilamos el proyecto (sin tests para acelerar)
RUN mvn clean package -DskipTests

# ================================
# 🚀 STAGE 2 - Runtime Stage
# ================================
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Establecer variables de entorno útiles
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
ENV TZ=America/Lima
ENV SPRING_PROFILES_ACTIVE=prod

# Copiamos el .jar desde el stage anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto Eureka (por defecto 8761)
EXPOSE 8761

# Metadata del contenedor (opcional pero buena práctica)
LABEL maintainer="TuNombre <tucorreo@dominio.com>" \
      version="1.0" \
      description="Eureka Discovery Server for microservices"

# Ejecutamos la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
