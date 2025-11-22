FROM eclipse-temurin:21-jdk-alpine

EXPOSE 8080

ENV JPDA_TRANSPORT=dt_socket
ENV JPDA_ADDRESS=8000

RUN mkdir -p /opt/Abbot

ADD build/libs/Abbbot-1.0.2.jar Abbot.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/Abbot.jar"]
