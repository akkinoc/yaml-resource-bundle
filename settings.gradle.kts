rootProject.name = "yaml-resource-bundle"

include("yaml-resource-bundle")
include("examples:simple-java")
include("examples:simple-kotlin")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
