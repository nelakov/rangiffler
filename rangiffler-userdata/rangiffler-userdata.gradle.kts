plugins {
    id("rangifflerbuild.spring-service-conventions")
}

dependencies {
    implementation(projects.rangifflerGrpcCommon)
    implementation(libs.spring.grpc.server.starter)
    implementation(libs.spring.data.jpa.starter)
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.kafka.starter)
    runtimeOnly(libs.mysql)
    runtimeOnly(libs.flyway)
    runtimeOnly(libs.flyway.mysql)
}

tasks.named<Jar>("bootJar") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
