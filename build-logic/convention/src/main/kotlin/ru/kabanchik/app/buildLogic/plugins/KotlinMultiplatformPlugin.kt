package ru.kabanchik.app.buildLogic.plugins

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.kabanchik.app.buildLogic.tools.libs

class KotlinMultiplatformPlugin: Plugin<Project> {
    override fun apply(target: Project):Unit = with(target){
        with(pluginManager){
            apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            apply(libs.findPlugin("androidLibrary").get().get().pluginId)
        }

        extensions.configure<KotlinMultiplatformExtension> {
            jvmToolchain(21)

            androidTarget()
            iosArm64()
            iosSimulatorArm64()
            jvm()

            //common dependencies
            sourceSets.apply {
                commonMain {
                    dependencies {
                        implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                    }
                }

                androidMain {
                    dependencies {
                        implementation(libs.findLibrary("kotlinx-coroutines-android").get())
                    }
                }
            }
        }

        extensions.configure<LibraryExtension> {
            compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
            defaultConfig {
                minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_21
                targetCompatibility = JavaVersion.VERSION_21
            }
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
        }
    }
}