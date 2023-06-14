FROM openjdk:8
EXPOSE 8080
ADD target/interviewJar.jar interviewJar.jar
ENTRYPOINT ["java","-jar","interviewJar.jar"]