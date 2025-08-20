FROM eclipse-temurin:17-jdk-alpine
COPY main-app/target/user.jar /user.jar
ENTRYPOINT ["java","-jar","/user.jar"]
EXPOSE 8080