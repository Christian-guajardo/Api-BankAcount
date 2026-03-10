FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x ./gradlew || true
RUN ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:25-jdk AS runtime
WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "/app/app.jar"]