import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage

plugins {
    id("com.bmuschko.docker-remote-api")
}

val dockerBaseImage = if (System.getProperty("os.arch") in listOf("aarch64", "arm64"))
    "arm64v8/eclipse-temurin:25-jre"
else
    "eclipse-temurin:25-jre"

tasks.register<DockerBuildImage>("dockerBuild") {
    dependsOn("build")
    inputDir.set(layout.projectDirectory)
    images.add("nelakov/${project.name}:${project.version}")
    images.add("nelakov/${project.name}:latest")
    buildArgs.put("APP_VER", project.version.toString())
    buildArgs.put("DOCKER", dockerBaseImage)
    noCache.set(true)
}
