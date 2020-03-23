FROM tomcat:alpine

EXPOSE 8080

ENV JPDA_TRANSPORT=dt_socket
ENV JPDA_ADDRESS=8000

RUN rm -rf webapps/ROOT
ADD ROOT.war webapps/

RUN mkdir -p /opt/Abbot

CMD ["catalina.sh", "run"]
