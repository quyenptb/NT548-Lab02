FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy file JAR đã được build bởi Jenkins vào
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]