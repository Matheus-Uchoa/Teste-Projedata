# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copia os arquivos do projeto teste-api
COPY teste-api/pom.xml ./pom.xml
COPY teste-api/src ./src

# Build do projeto
RUN mvn clean package -DskipTests -B

# Etapa de runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o diretório quarkus-app completo
COPY --from=build /app/target/quarkus-app /app

# Porta padrão
EXPOSE 8080

# Variáveis de ambiente
ENV QUARKUS_HTTP_HOST=0.0.0.0

# Start da aplicação Quarkus
CMD java -Dquarkus.http.host=0.0.0.0 -Dquarkus.http.port=${PORT:-8080} -jar quarkus-run.jar
