dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}

rootProject.name = "plugins"

include("common")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
