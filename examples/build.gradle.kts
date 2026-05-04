subprojects {
    configurations.configureEach {
        resolutionStrategy.dependencySubstitution {
            substitute(module("dev.akkinoc.util:yaml-resource-bundle")).using(project(":core"))
        }
    }
}
