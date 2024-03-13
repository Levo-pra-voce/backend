FROM ghcr.io/graalvm/native-image:ol8-java17-22 AS builder

# Install tar and gzip to extract the Maven binaries
RUN microdnf update \
 && microdnf install --nodocs \
    tar \
    gzip \
 && microdnf clean all \
 && rm -rf /var/cache/yum

WORKDIR /app
COPY . .
# Cache Gradle and dependencies
RUN ./gradlew clean --no-daemon
# Compile native executable
RUN ./gradlew nativeCompile --no-daemon

# Create run image from scratch
FROM alpine:3.14

COPY --from=builder /app/build/native/nativeCompile/levo-pra-voce .
ENTRYPOINT ["./levo-pra-voce"]
#COPY --from=builder /app/build/native/nativeCompile/levo-pra-voce .
#ENTRYPOINT ["/levo-pra-voce"]