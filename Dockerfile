FROM dockerhub.cisco.com/iot-dockerpreprod/iot-alpine-images/iot-alpine-jre-oracle:3.3_8.91
MAINTAINER SupplyByte Inc.
ENV APP_NAME=sb-services-common
RUN mkdir -p /${APP_NAME}
WORKDIR /$APP_NAME

COPY ./build/libs/${APP_NAME}.jar ./${APP_NAME}.jar
COPY ./config.properties ./config.properties
EXPOSE 8080
