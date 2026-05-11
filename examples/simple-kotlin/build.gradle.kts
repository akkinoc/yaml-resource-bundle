plugins {
    application
    kotlin("jvm")
}

dependencies {
    implementation("dev.akkinoc.util:yaml-resource-bundle:$version")
}

application {
    mainClass = "example.ApplicationKt"
}
