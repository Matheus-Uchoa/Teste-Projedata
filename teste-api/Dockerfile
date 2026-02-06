# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copia arquivos Maven para cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código fonte e faz o build
COPY src/ ./src/
RUN mvn package -DskipTests -B

# Etapa de runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o jar gerado
COPY --from=build /app/target/testeprojedata-1.0-SNAPSHOT.jar app.jar

# Porta padrão
EXPOSE 8080

# Variáveis de ambiente podem ser sobrescritas pelo Render
ENV QUARKUS_HTTP_HOST=0.0.0.0
ENV QUARKUS_HTTP_PORT=8080

# Start da aplicação com suporte à variável PORT do Render
CMD java -Dquarkus.http.port=${PORT:-8080} -jar app.jar
