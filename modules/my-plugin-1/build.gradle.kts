plugins {
    kotlin("jvm") version "1.8.21"
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":modules:plugin-context"))
}