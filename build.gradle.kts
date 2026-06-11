plugins {
    java
}

group = "com.elakov"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

subprojects {
    extra["dockerImage"] = if (System.getProperty("os.arch") in listOf("aarch64", "arm64"))
        "arm64v8/eclipse-temurin:25-jre"
    else
        "eclipse-temurin:25-jre"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
