plugins {
    id("rangifflerbuild.spring-service-conventions")
}

dependencies {
    implementation(projects.rangifflerGrpcCommon)
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
