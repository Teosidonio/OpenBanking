FROM java:8-jdk-alpine
COPY ./target/e_biller-0.0.1-SNAPSHOT.war /usr/app/
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "e_biller-0.0.1-0.0.1-SNAPSHOT.war"]