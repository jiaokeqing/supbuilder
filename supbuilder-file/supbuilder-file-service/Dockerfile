FROM moxm/java:1.8-full

RUN mkdir -p /supbuilder-file-service

WORKDIR /supbuilder-file-service

ARG JAR_FILE=target/supbuilder-file-service.jar

COPY ${JAR_FILE} app.jar

EXPOSE 4000

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom"

CMD sleep 60; java $JAVA_OPTS -jar app.jar
