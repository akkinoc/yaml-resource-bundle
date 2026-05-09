plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation("dev.akkinoc.util:yaml-resource-bundle")
}

application {
    mainClass = "example.ApplicationKt"
}
