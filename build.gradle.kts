plugins {
    java
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
    jacoco
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

apply("gradle/swagger.gradle.kts")
group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "BankAccountManager"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"]= "2023.0.3"
dependencyManagement{
    imports{
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}
dependencies{
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("io.github.openfeign:feign-jackson")
    implementation("io.github.openfeign:feign-okhttp")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.core:jackson-core")
    testImplementation("io.github.openfeign:feign-mock")

    implementation("com.fasterxml.jackson.core:jackson-annotations:2.20")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-jersey")
    // Spring Data JPA
    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // https://mvnrepository.com/artifact/com.h2database/h2
    implementation("com.h2database:h2:2.4.240")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")


    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    implementation("io.jsonwebtoken:jjwt-impl:0.13.0")
    implementation("io.jsonwebtoken:jjwt-jackson:0.13.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
