plugins {
    alias(libs.plugins.kotlin)
}

dependencies {
    api(libs.snakeyaml)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter(libs.junit.jupiter.map { it.version })
            dependencies {
                implementation(libs.kotest.assertions)
            }
        }
    }
}
