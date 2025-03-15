group = "com.kkw"
version = "0.0.1-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.jetbrains.kotlin.kapt") version "1.9.25"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

dependencies {
    // ✅ Spring Boot Starter Dependencies
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-batch")

    // ✅ QueryDSL JPA 지원
    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

    // ✅ QueryDSL Kotlin 지원
    implementation("com.querydsl:querydsl-kotlin:5.0.0")

    // ✅ QueryDSL 컴파일 시 Annotation Processing 활성화 (Kotlin KAPT 사용)
    kapt("jakarta.persistence:jakarta.persistence-api:3.1.0")
    kapt("jakarta.annotation:jakarta.annotation-api:2.1.0")

    // ✅ Jackson + Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // ✅ Kotlin Reflect (Spring에서 Kotlin 사용 시 필수)
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // ✅ Auth0 JWT (HMAC256, RSA 등 사용 가능)
    implementation("com.auth0:java-jwt:4.4.0")

    // ✅ JWT Authentication (최신 jjwt 라이브러리 사용)
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // ✅ AWS SDK for S3
    implementation("software.amazon.awssdk:s3:2.20.74")

    // ✅ Database Connector
    runtimeOnly("com.mysql:mysql-connector-j")

    // ✅ Springdoc OpenAPI (Swagger UI 추가)
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // ✅ Test Dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework:spring-webflux")
    testImplementation("org.springframework.batch:spring-batch-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    outputs.dir(project.extra["snippetsDir"]!!)
}

tasks.asciidoctor {
    inputs.dir(project.extra["snippetsDir"]!!)
    dependsOn(tasks.test)
}

sourceSets {
    main {
        java.srcDirs("src/main/kotlin", "src/main/generated")
    }
}
