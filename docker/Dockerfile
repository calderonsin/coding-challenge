FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy everything and build
COPY .. .
RUN chmod +x gradlew && \
     ./gradlew clean bootJar --no-daemon && \
     find build/libs -name "*.jar" -exec cp {} app.jar \;

# Expose port and run
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]