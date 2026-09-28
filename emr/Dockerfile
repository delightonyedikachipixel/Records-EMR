# ---- Stage 1: Build ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy Gradle wrapper and build files first (better layer caching)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew

# Pre-fetch dependencies so they're cached in their own layer
RUN ./gradlew dependencies --no-daemon || true

# Now copy source and build the jar
COPY src src
RUN ./gradlew clean bootJar --no-daemon -x test

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Run as a non-root user
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
