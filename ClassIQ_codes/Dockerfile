

FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests


FROM eclipse-temurin:21-jdk
WORKDIR /app

# GUI/X11 libraries
RUN apt-get update && apt-get install -y \
    libx11-6 libxext6 libxrender1 libxtst6 libxi6 libgtk-3-0 mesa-utils wget unzip \
    && rm -rf /var/lib/apt/lists/*

# JavaFX SDK (Linux)
RUN mkdir -p /javafx-sdk \
    && wget -O javafx.zip https://download2.gluonhq.com/openjfx/21/openjfx-21_linux-x64_bin-sdk.zip \
    && unzip javafx.zip -d /javafx-sdk \
    && mv /javafx-sdk/javafx-sdk-21/lib /javafx-sdk/lib \
    && rm -rf /javafx-sdk/javafx-sdk-21 javafx.zip

# built jar
COPY --from=build /app/target/ClassIQ-1.0-SNAPSHOT.jar app.jar

# Windows Xming/VcXsrv
ENV DISPLAY=host.docker.internal:0.0

CMD ["java", "--module-path", "/javafx-sdk/lib", "--add-modules", "javafx.controls,javafx.fxml,javafx.graphics", "-jar", "app.jar"]