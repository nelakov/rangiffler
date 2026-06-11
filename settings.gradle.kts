pluginManagement {
    includeBuild("gradle/plugins")
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "rangiffler"

include("rangiffler-gateway")
include("rangiffler-auth")
include("rangiffler-userdata")
include("rangiffler-country")
include("rangiffler-grpc-common")
include("rangiffler-photo")
include("rangiffler-e-2-e-tests")

// Every subproject has a build file named after the project (junit5 convention).
rootProject.children.forEach { project ->
    project.buildFileName = "${project.name}.gradle.kts"
    require(project.buildFile.isFile) {
        "${project.buildFile} must exist"
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
