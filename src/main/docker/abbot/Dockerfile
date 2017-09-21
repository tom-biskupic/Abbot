FROM tomcat:alpine

EXPOSE 8080

ENV JPDA_TRANSPORT=dt_socket
ENV JPDA_ADDRESS=8000

ADD Abbot3.war webapps/

RUN mkdir -p /opt/Abbot

CMD ["catalina.sh", "run"]
