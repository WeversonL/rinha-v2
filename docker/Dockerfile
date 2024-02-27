FROM ghcr.io/graalvm/native-image-community:21 AS build
WORKDIR /app
COPY ./app /app
RUN DB_URL=r2dbc:postgresql://localhost:5432/rinhadb DB_USER=rinha DB_PASS=rinha DB_POOL=40 ./mvnw -Pnative native:compile

FROM alpine:3.19.1
COPY --from=build /app/target/rinhav2 /rinhav2
RUN apk --no-cache add libc6-compat
EXPOSE 8080
CMD ["./rinhav2"]
