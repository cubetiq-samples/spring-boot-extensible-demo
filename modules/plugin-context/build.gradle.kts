plugins {
    kotlin("jvm") version "1.8.10"
}

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/jakarta.servlet/jakarta.servlet-api
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    // https://mvnrepository.com/artifact/org.reflections/reflections
    implementation("org.reflections:reflections:0.10.2")
}