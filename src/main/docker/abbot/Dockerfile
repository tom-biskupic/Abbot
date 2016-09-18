FROM centos

RUN yum -y install wget
RUN	cd /opt/;\
       wget --no-cookies --no-check-certificate \
            --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie"\
            "http://download.oracle.com/otn-pub/java/jdk/8u101-b13/jdk-8u101-linux-x64.tar.gz";\
    tar xzf jdk-8u101-linux-x64.tar.gz
    
ENV JAVA_HOME=/opt/jdk1.8.0_101/
ENV PATH=$PATH:$JAVA_HOME/bin

ENV TOMCAT_MIRROR http://mirror.ventraip.net.au/apache
ENV TOMCAT_MAJOR 8
ENV TOMCAT_VERSION 8.0.37
ENV TOMCAT_TGZ_URL $TOMCAT_MIRROR/tomcat/tomcat-$TOMCAT_MAJOR/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz

ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

RUN mkdir -p "$CATALINA_HOME"
WORKDIR $CATALINA_HOME

RUN	wget "$TOMCAT_TGZ_URL" && \
	tar -xvf apache-tomcat-$TOMCAT_VERSION.tar.gz --strip-components=1 && \
	rm bin/*.bat &&\
	rm apache-tomcat-$TOMCAT_VERSION.tar.gz
    
EXPOSE 8080

ADD Abbot3.war webapps/

CMD ["catalina.sh", "run"]
