import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    java
    alias(libs.plugins.allure.report)
    alias(libs.plugins.allure.adapter)
    alias(libs.plugins.lombok)
}

group = libs.versions.group.get()
version = libs.versions.app.get()

repositories {
    gradlePluginPortal()
    mavenCentral()
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
    testImplementation(project(":rangiffler-grpc-common"))
    testImplementation(libs.junit5.jupiter)
    testImplementation(libs.bundles.selenide)
    testImplementation(libs.bundles.allure)
    testImplementation(libs.bundles.rest.assured)
    testImplementation(libs.bundles.logs)
    testImplementation(libs.bundles.grpc)
    testImplementation(libs.bundles.database)
    testImplementation(libs.bundles.test.utils)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    // Test JVM is forked: propagate -Denv so @Env-gated tests are not silently skipped
    System.getProperty("env")?.let { systemProperty("env", it) }
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
    }
}
