# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copia tudo para dentro do container
COPY . .

# Build do projeto
RUN mvn package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o jar gerado
COPY --from=build /app/target/*SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
