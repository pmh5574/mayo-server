FROM public.ecr.aws/docker/library/amazoncorretto:17

USER root

ARG JAR_FILE
ARG SPRING_PROFILES_ACTIVE
ARG JWT_SECRET_KEY
ARG MARIA_PASSWORD
ARG MARIA_USERNAME
ARG SERVER_HOST
ARG SHA_256_SALT
ARG AWS_CF_DISTRIBUTION
ARG AWS_ACCESS_KEY
ARG AWS_SECRET_KEY

ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
ENV JWT_SECRET_KEY=${JWT_SECRET_KEY}
ENV MARIA_PASSWORD=${MARIA_PASSWORD}
ENV MARIA_USERNAME=${MARIA_USERNAME}
ENV SERVER_HOST=${SERVER_HOST}
ENV SHA_256_SALT=${SHA_256_SALT}
ENV AWS_CF_DISTRIBUTION=${AWS_CF_DISTRIBUTION}
ENV AWS_ACCESS_KEY=${AWS_ACCESS_KEY}
ENV AWS_SECRET_KEY=${AWS_SECRET_KEY}

WORKDIR /app

COPY ${JAR_FILE} app.jar

EXPOSE 8081

CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]