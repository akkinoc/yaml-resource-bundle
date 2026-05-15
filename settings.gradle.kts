plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "yaml-resource-bundle"

include("yaml-resource-bundle")
include("examples:simple-java")
include("examples:simple-kotlin")
