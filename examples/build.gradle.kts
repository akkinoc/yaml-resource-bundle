subprojects {
    configurations.configureEach {
        resolutionStrategy.dependencySubstitution {
            "yaml-resource-bundle".also { substitute(module("$group:$it")).using(project(":$it")) }
        }
    }
}
