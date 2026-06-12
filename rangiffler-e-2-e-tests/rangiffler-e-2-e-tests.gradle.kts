import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    id("rangifflerbuild.java-conventions")
    alias(libs.plugins.allure.report)
    alias(libs.plugins.allure.adapter)
    alias(libs.plugins.lombok)
}

repositories {
    gradlePluginPortal()
}

allure {
    report {
        version.set(libs.versions.allure.bom.get())
    }
    adapter {
        autoconfigure.set(true)
        aspectjWeaver.set(true)
        aspectjVersion.set(libs.versions.aspectj.get())
        frameworks {
            junit5 {
                adapterVersion.set(libs.versions.allure.bom.get())
            }
        }
    }
}

dependencies {
    implementation(libs.jakarta.validation)
    implementation(libs.jakarta.annotation)
    testImplementation(projects.rangifflerGrpcCommon)
    testImplementation(libs.junit5.jupiter)
    testImplementation(libs.bundles.selenide)
    testImplementation(libs.bundles.allure)
    testImplementation(libs.bundles.rest.assured)
    testImplementation(libs.bundles.logs)
    testImplementation(libs.bundles.grpc)
    testImplementation(libs.bundles.database)
    testImplementation(libs.bundles.test.utils)
}

tasks.withType<Test>().configureEach {
    // Test JVM is forked: propagate -Denv so @Env-gated tests are not silently skipped,
    // plus the remote-browser / Selenoid-video overrides so a local Grid run works via
    // -Dbrowser.remote=... -Dvideo.storage=...
    listOf("env", "browser.remote", "video.storage").forEach { key ->
        System.getProperty(key)?.let { systemProperty(key, it) }
    }
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
    }
}
