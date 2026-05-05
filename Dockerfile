# --- ESTÁGIO 1: A Fábrica (Build) ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
# Copia apenas o arquivo de dependências primeiro (otimiza o cache do Docker)
COPY pom.xml .
# Baixa as dependências do Spring Boot
RUN mvn dependency:go-offline
# Copia o código fonte e compila o projeto ignorando os testes para ser rápido
COPY src ./src
RUN mvn clean package -DskipTests

# --- ESTÁGIO 2: O Palco (Run) ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Copia apenas o arquivo .jar compilado da "Fábrica" (deixa o container extremamente leve)
COPY --from=builder /app/target/*.jar app.jar

# Informa ao Google que a API está pronta para ouvir na porta padrão 8080
EXPOSE 8080

# O comando exato de ignição
ENTRYPOINT ["java", "-Dserver.port=${PORT:8080}", "-jar", "app.jar"]