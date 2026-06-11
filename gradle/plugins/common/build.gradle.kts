plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    // Plugins applied by the convention plugins must be on the compile classpath.
    // Marker artifacts (`<id>:<id>.gradle.plugin`) resolve to the real plugin jars,
    // versions sourced from the shared catalog.
    implementation("org.springframework.boot:org.springframework.boot.gradle.plugin:${libs.versions.springframework.boot.get()}")
    implementation("io.spring.dependency-management:io.spring.dependency-management.gradle.plugin:${libs.versions.spring.dependency.management.get()}")
    implementation("io.freefair.lombok:io.freefair.lombok.gradle.plugin:${libs.versions.lombok.get()}")
    implementation("com.bmuschko.docker-remote-api:com.bmuschko.docker-remote-api.gradle.plugin:${libs.versions.docker.bmuschko.get()}")
}
