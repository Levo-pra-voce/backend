/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    java
    id("org.graalvm.buildtools.native") version "0.9.28"
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

repositories {
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
        url = uri("https://raw.githubusercontent.com/graalvm/native-build-tools/snapshots")
    }
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // LOMBOK
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.postgresql:postgresql")

    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.5")

    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.mapstruct:mapstruct:1.5.0.Final")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mockito:mockito-core")

    runtimeOnly("ch.qos.logback:logback-classic")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation(kotlin("stdlib-jdk8"))
    runtimeOnly("com.h2database:h2")
    implementation("com.h2database:h2:1.3.148")
}

group = "com.levo-pra-voce"
version = "0.0.1-SNAPSHOT"
description = "levo pra voce backend"

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

java {
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.jar {
    manifest {
        attributes(
                "Main-Class" to "com.levopravoce.backend.BackendApplication",
        )
    }
}

//graalvmNative {
//    binaries {
//        named("main") {
//            imageName.set("levo-pra-voce")
//            mainClass.set("com.levopravoce.backend.BackendApplication")
//            buildArgs.add("-O4")
//            javaLauncher.set(javaToolchains.launcherFor {
//                languageVersion.set(JavaLanguageVersion.of(17))
//                vendor.set(JvmVendorSpec.matching("GraalVM"))
//            })
//        }
//    }
//    binaries.all {
//        buildArgs.add("--verbose")
//    }
//}