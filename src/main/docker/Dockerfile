FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp

ADD its.jar app.jar
RUN sh -c 'touch /app.jar'

ADD scouter /scouter

ENV JAVA_OPTS="-javaagent:/scouter/agent.java/scouter.agent.jar"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]