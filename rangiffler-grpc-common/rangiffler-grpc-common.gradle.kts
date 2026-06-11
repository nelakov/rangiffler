import com.google.protobuf.gradle.id

plugins {
    id("rangifflerbuild.java-conventions")
    `java-library`
    alias(libs.plugins.google.protobuf)
}

dependencies {
    // api: generated stubs + shared tracing interceptors expose gRPC + slf4j types to consumers
    api(libs.bundles.grpc)
    api(libs.slf4j.api)
    compileOnly(libs.javax.annotation)
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.version.get()}"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.version.get()}"
        }
    }
    generateProtoTasks {
        all().forEach { it.plugins { id("grpc") } }
    }
}

idea {
    module {
        generatedSourceDirs.add(file("${layout.buildDirectory.get()}/generated/source/proto/main/java"))
        generatedSourceDirs.add(file("${layout.buildDirectory.get()}/generated/source/proto/main/grpc"))
    }
}
