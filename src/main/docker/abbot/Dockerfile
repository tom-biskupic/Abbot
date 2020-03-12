FROM tomcat:alpine

EXPOSE 8080

ENV JPDA_TRANSPORT=dt_socket
ENV JPDA_ADDRESS=8000

RUN rm -rf webapps/ROOT
ADD ROOT.war webapps/

RUN echo "dbhostname=abbot-mysql" >> conf/catalina.properties
RUN echo "dbuser=AbbotUser" >> conf/catalina.properties
RUN echo "dbpassword=password" >> conf/catalina.properties
RUN echo "dbname=AbbotDB" >> conf/catalina.properties

RUN mkdir -p /opt/Abbot

CMD ["catalina.sh", "run"]
