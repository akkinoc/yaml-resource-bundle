rootProject.name = "yaml-resource-bundle"

include("core")
include("examples:simple-java")
include("examples:simple-kotlin")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
