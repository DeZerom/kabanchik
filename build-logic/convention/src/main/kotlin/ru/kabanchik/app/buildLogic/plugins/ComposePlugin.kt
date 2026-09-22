package ru.kabanchik.app.buildLogic.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.kabanchik.app.buildLogic.tools.androidLibrary
import ru.kabanchik.app.buildLogic.tools.libs

class ComposePlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply(libs.findPlugin("composeMultiplatform").get().get().pluginId)
                apply(libs.findPlugin("composeCompiler").get().get().pluginId)
                apply(libs.findPlugin("composeHotReload").get().get().pluginId)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                // Нужны для Compose Resources на Android: новый KMP-плагин по умолчанию их не собирает
                androidLibrary {
                    androidResources {
                        enable = true
                    }
                }

                sourceSets.apply {
                    commonMain {
                        dependencies {
                            implementation(libs.findLibrary("runtime").get())
                            implementation(libs.findLibrary("foundation").get())
                            implementation(libs.findLibrary("material3").get())
                            implementation(libs.findLibrary("ui").get())
                            implementation(libs.findLibrary("components-resources").get())
                            implementation(libs.findLibrary("ui-tooling-preview").get())
                        }
                    }
                }
            }

            dependencies {
                // У com.android.kotlin.multiplatform.library нет build types, поэтому не debugImplementation.
                // runtimeClasspath не транзитивен, в потребителей ui-tooling не попадёт
                add("androidRuntimeClasspath", libs.findLibrary("ui-tooling").get())
            }
        }
    }
}