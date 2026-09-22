package ru.kabanchik.app.buildLogic.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import ru.kabanchik.app.buildLogic.tools.androidLibrary
import ru.kabanchik.app.buildLogic.tools.libs

class KotlinMultiplatformPlugin: Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager){
            apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
            apply(libs.findPlugin("androidMultiplatformLibrary").get().get().pluginId)
        }

        extensions.configure<KotlinMultiplatformExtension> {
            jvmToolchain(21)

            androidLibrary {
                compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
                minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()

                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_21)
                }

                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }

                // commonTest также гоняется как android unit-тесты, как было с com.android.library
                withHostTest {}
            }
            iosArm64()
            iosSimulatorArm64()
            jvm()

            //common dependencies
            sourceSets.apply {
                commonMain {
                    dependencies {
                        implementation(libs.findLibrary("kotlinx-coroutines-core").get())
                        implementation(libs.findLibrary("shivathapaa-logger").get())
                    }
                }

                androidMain {
                    dependencies {
                        implementation(libs.findLibrary("kotlinx-coroutines-android").get())
                    }
                }
            }
        }
    }
}
