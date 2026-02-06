# Etapa de build
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Debug: mostra o conteúdo do diretório atual antes de copiar
RUN pwd && ls -la || true

# Copia tudo de uma vez
COPY . .

# Debug: mostra o que foi copiado
RUN echo "=== Arquivos copiados ===" && ls -la && echo "=== Verificando pom.xml ===" && ls -la pom.xml || echo "POM.XML NÃO ENCONTRADO!"

# Build do projeto
RUN mvn clean package -DskipTests -B

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
