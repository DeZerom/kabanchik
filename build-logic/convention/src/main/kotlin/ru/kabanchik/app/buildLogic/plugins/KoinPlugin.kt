package ru.kabanchik.app.buildLogic.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.kabanchik.app.buildLogic.tools.libs

class KoinPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain {
                        dependencies {
                            implementation(libs.findLibrary("koin-core").get())
                        }
                    }
                    commonTest {
                        dependencies {
                            implementation(libs.findLibrary("koin-test").get())
                        }
                    }
                }
            }
        }
    }
}