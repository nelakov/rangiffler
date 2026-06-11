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
    implementation(libs.spring.data.jpa.starter)
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.kafka.starter)
    runtimeOnly(libs.mysql)
    runtimeOnly(libs.flyway)
    runtimeOnly(libs.flyway.mysql)
    testImplementation(libs.spring.test.starter)
}

tasks.register<DockerBuildImage>("dockerBuild") {
    dependsOn("build")
    inputDir.set(layout.projectDirectory)
    images.add("nelakov/rangiffler-userdata:${project.version}")
    images.add("nelakov/rangiffler-userdata:latest")
    buildArgs.put("APP_VER", project.version.toString())
    buildArgs.put("DOCKER", project.extra["dockerImage"].toString())
    noCache.set(true)
}

tasks.named<Jar>("bootJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
