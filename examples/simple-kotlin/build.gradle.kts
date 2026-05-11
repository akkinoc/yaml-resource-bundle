plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation("dev.akkinoc.util:yaml-resource-bundle")
}

application {
    mainClass = "example.ApplicationKt"
}
