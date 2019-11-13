FROM openjdk:8
ADD target/Organization-Charts.jar Organization-Charts.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","Organization-Charts.jar"]