WORKDIR /app
COPY . .
RUN chmod +x ./gradlew

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]