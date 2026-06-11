plugins {
    id("rangifflerbuild.java-conventions")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("io.freefair.lombok")
    id("rangifflerbuild.docker-conventions")
}

dependencies {
    "testImplementation"(dependencyFromLibs("spring-test-starter"))
}
