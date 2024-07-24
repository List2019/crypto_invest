FROM openjdk:21
EXPOSE 8080
ARG CRYPTO_DATA_PATH=/app/crypto_data
COPY ./crypto_data ${CRYPTO_DATA_PATH}
ADD target/crypto-invest-1.0.jar app.jar
ENV crypto.data.path=${CRYPTO_DATA_PATH}
ENTRYPOINT ["java","-jar","/app.jar"]
