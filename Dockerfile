FROM redis
COPY redis.conf /usr/local/etc/redis/redis.conf
CMD [ "redis-server", "/usr/local/etc/redis/redis.conf" ]

FROM openjdk
COPY target/spring-boot-complete-0.0.1-SNAPSHOT.jar /spring-app.jar
COPY resources/application-jenkins.properties /application-jenkins.properties
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=jenkins", "/spring-app.jar", "--spring.config.location=/application-jenkins.properties"]