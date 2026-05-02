plugins {
    application
    kotlin("jvm") version "2.3.21"
}

dependencies {
    implementation("dev.akkinoc.util:yaml-resource-bundle:2.16.2")
}

application {
    mainClass = "example.ApplicationKt"
}
