plugins {
    kotlin("jvm") version "1.8.22"
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":modules:plugin-context"))

    // https://mvnrepository.com/artifact/org.springframework/spring-context
    implementation("org.springframework:spring-context:6.0.10")
}