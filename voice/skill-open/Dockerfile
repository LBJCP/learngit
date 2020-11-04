FROM java:8
VOLUME /tmp
EXPOSE 12000
ADD /target/skill-open-0.0.1-SNAPSHOT.jar skillopen.jar
RUN bash -c "touch /skillopen.jar"
RUN  /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
ENTRYPOINT ["java","-jar","/skillopen.jar"]
