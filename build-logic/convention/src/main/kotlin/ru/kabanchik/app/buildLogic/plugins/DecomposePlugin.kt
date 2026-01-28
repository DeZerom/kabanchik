package ru.kabanchik.app.buildLogic.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.kabanchik.app.buildLogic.tools.libs

class DecomposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply(libs.findPlugin("kotlinx-serialization").get().get().pluginId)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.apply {
                    commonMain {
                        dependencies {
                            implementation(libs.findLibrary("decompose").get())
                            implementation(libs.findLibrary("decompose-extensionsCompose").get())
                            implementation(libs.findLibrary("essenty").get())
                            implementation(libs.findLibrary("essenty-coroutines").get())
                            implementation(libs.findLibrary("kotlinx-serializationJson").get())
                        }
                    }

                    androidMain {
                        dependencies {
                            implementation(libs.findLibrary("decompose").get())
                        }
                    }
                }
            }
        }
    }
}