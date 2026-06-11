import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

plugins {
    java
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.lombok)
    alias(libs.plugins.docker)
}

group = libs.versions.group.get()
version = libs.versions.app.get()

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(project(":rangiffler-grpc-common"))
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.webflux.starter)
    implementation(libs.spring.actuator.starter)
    implementation(libs.spring.validation.starter)
    implementation(libs.spring.oauth2.starter)
    implementation(libs.spring.security.starter)
    implementation(libs.spring.boot.services.starter) {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation(libs.spring.grpc.client.starter)
    implementation(libs.reactor.netty)
    runtimeOnly(libs.commons.logging)
    testImplementation(libs.spring.test.starter)
}

tasks.register<DockerBuildImage>("dockerBuild") {
    dependsOn("build")
    inputDir.set(layout.projectDirectory)
    images.add("nelakov/rangiffler-gateway:${project.version}")
    images.add("nelakov/rangiffler-gateway:latest")
    buildArgs.put("APP_VER", project.version.toString())
    buildArgs.put("DOCKER", project.extra["dockerImage"].toString())
    noCache.set(true)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register<Test>("runTestsByTag") {
    val testTag = System.getProperty("test")
    if (testTag != null) {
        useJUnitPlatform { includeTags(testTag) }
    } else {
        useJUnitPlatform()
    }
    @Suppress("UNCHECKED_CAST")
    systemProperties(System.getProperties() as Map<String, Any>)
}
