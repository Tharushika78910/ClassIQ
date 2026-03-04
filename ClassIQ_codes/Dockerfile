# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only pom.xml first (stable dependency layer)
COPY pom.xml .

RUN mvn -B dependency:go-offline

# Copy source after dependencies cached
COPY src ./src

RUN mvn -B clean package -DskipTests


# Stage 2: Runtime (small & safe)
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/target/ClassIQ-1.0-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]