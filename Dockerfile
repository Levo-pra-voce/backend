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
FROM debian:12.0-slim

WORKDIR /app
# Copy the native executable
COPY --from=builder /app/build/native/nativeCompile/levo-pra-voce /app/levo-pra-voce
# Run the native executable
#CMD ["/app/levo-pra-voce"]