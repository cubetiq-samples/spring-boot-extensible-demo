plugins {
    kotlin("jvm") version "1.7.10"
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":modules:plugin-context"))
}