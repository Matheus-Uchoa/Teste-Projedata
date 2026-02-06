# Etapa de build
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copia tudo para dentro do container
COPY . .

# Build do projeto
RUN ./mvnw package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o jar gerado
COPY --from=build /app/target/testeprojedata-1.0-SNAPSHOT.jar app.jar

# Porta padrão (Render usa PORT automaticamente)
EXPOSE 8080

# Start da aplicação
CMD ["java", "-jar", "app.jar"]
