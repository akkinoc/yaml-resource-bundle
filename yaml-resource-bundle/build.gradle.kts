import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SourcesJar

plugins {
    `java-library`
    jacoco
    alias(libs.plugins.kotlin)
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
    alias(libs.plugins.publish)
}

dependencies {
    api(libs.snakeyaml)
    detektPlugins(libs.detekt.rules.ktlint)
}

java {
    toolchain {
        languageVersion = libs.versions.java.asProvider().map { JavaLanguageVersion.of(it) }
    }
}

kotlin {
    compilerOptions {
        javaParameters = true
    }
}

detekt {
    buildUponDefaultConfig = true
    config.from(rootProject.file("config/detekt/config.yml"))
}

testing {
    suites {
        val test by getting(JvmTestSuite::class)
        withType<JvmTestSuite> {
            useJUnitJupiter(libs.junit.jupiter.map { it.version })
            dependencies {
                implementation(libs.kotest.assertions)
            }
        }
        libs.versions.java.additional.get().split(",").map { JavaLanguageVersion.of(it) }.forEach { javaVersion ->
            register<JvmTestSuite>("testOnJava$javaVersion") {
                dependencies {
                    implementation(project())
                }
                sources {
                    java {
                        srcDirs(test.sources.java.srcDirs)
                    }
                    kotlin {
                        srcDirs(test.sources.kotlin.srcDirs)
                    }
                    resources {
                        srcDirs(test.sources.resources.srcDirs)
                    }
                }
                targets.all {
                    testTask {
                        javaLauncher = javaToolchains.launcherFor {
                            languageVersion = javaVersion
                        }
                    }
                }
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.withType<Test>())
}

tasks.withType<Test> {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    executionData(*tasks.withType<Test>().toTypedArray())
    reports {
        xml.required = true
        html.required = true
    }
}

dokka {
    dokkaSourceSets.configureEach {
        includes.from(sourceSets.main.get().kotlin.srcDirs.map { fileTree(it) { include("**/*.md") } })
    }
}

mavenPublishing {
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
    configure(
        KotlinJvm(
            sourcesJar = SourcesJar.Sources(),
            javadocJar = JavadocJar.Dokka(tasks.dokkaGeneratePublicationJavadoc),
        ),
    )
    pom {
        name = project.name
        description = "Java ResourceBundle for YAML format."
        url = "https://github.com/akkinoc/yaml-resource-bundle"
        inceptionYear = "2015"
        organization {
            name = "akkinoc.dev"
            url = "https://akkinoc.dev"
        }
        developers {
            developer {
                id = "akkinoc"
                name = "Akihiro Kondo"
                email = "akkinoc@gmail.com"
                url = "https://akkinoc.dev"
            }
        }
        licenses {
            licenses {
                license {
                    name = "Apache-2.0"
                    url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                }
            }
        }
        scm {
            url = "https://github.com/akkinoc/yaml-resource-bundle"
            connection = "scm:git:https://github.com/akkinoc/yaml-resource-bundle.git"
            developerConnection = "scm:git:ssh://git@github.com/akkinoc/yaml-resource-bundle.git"
        }
        issueManagement {
            system = "GitHub Issues"
            url = "https://github.com/akkinoc/yaml-resource-bundle/issues"
        }
        ciManagement {
            system = "GitHub Actions"
            url = "https://github.com/akkinoc/yaml-resource-bundle/actions"
        }
    }
}
