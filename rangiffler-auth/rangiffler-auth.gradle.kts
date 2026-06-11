plugins {
    id("rangifflerbuild.spring-service-conventions")
}

dependencies {
    implementation(libs.bundles.spring.boot.starter.auth)
    implementation(libs.spring.data.jpa.starter)
    implementation(libs.spring.kafka.starter)
    runtimeOnly(libs.mysql)
    runtimeOnly(libs.commons.logging)
    runtimeOnly(libs.flyway)
    runtimeOnly(libs.flyway.mysql)
}
