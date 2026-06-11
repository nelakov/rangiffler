plugins {
    id("rangifflerbuild.spring-service-conventions")
}

dependencies {
    implementation(projects.rangifflerGrpcCommon)
    implementation(libs.spring.grpc.server.starter)
    implementation(libs.spring.data.jpa.starter)
    runtimeOnly(libs.mysql)
    runtimeOnly(libs.flyway)
    runtimeOnly(libs.flyway.mysql)
}
