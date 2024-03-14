# SETUP NATIVE BINARY
FROM ghcr.io/graalvm/native-image-community:21 AS build
WORKDIR /app
COPY ./app /app
RUN DB_URL=r2dbc:postgresql://localhost:5432/rinhadb DB_USER=rinha DB_PASS=rinha ./mvnw -Pnative native:compile

# EXECUTE API
FROM alpine:3.19.1

ENV HOME_USER=/home/app
ENV APP_HOME=$HOME_USER/api

RUN addgroup -g 1001 app && adduser --disabled-password -G app -u 1000 app && \
    mkdir -p $APP_HOME && \
    chown -R app:app $HOME_USER && \
    apk --no-cache add libc6-compat

COPY --from=build /app/target/rinhav2 $APP_HOME/rinhav2

EXPOSE ${SERVER_PORT:-8080}

USER app

WORKDIR $APP_HOME

ENTRYPOINT ["./rinhav2"]