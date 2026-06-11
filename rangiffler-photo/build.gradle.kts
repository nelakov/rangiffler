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
    implementation(libs.spring.grpc.server.starter)
    implementation(libs.spring.grpc.client.starter)
    implementation(libs.spring.data.jpa.starter)
    runtimeOnly(libs.mysql)
    runtimeOnly(libs.flyway)
    runtimeOnly(libs.flyway.mysql)
    testImplementation(libs.spring.test.starter)
}

tasks.register<DockerBuildImage>("dockerBuild") {
    dependsOn("build")
    inputDir.set(layout.projectDirectory)
    images.add("nelakov/rangiffler-photo:${project.version}")
    images.add("nelakov/rangiffler-photo:latest")
    buildArgs.put("APP_VER", project.version.toString())
    buildArgs.put("DOCKER", project.extra["dockerImage"].toString())
    noCache.set(true)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
