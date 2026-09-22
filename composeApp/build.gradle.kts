
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "ru.kabanchik.client.composeApp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }

        androidResources {
            enable = true
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.uiKit)
            implementation(projects.common.network)
            implementation(projects.common.dataStore)
            implementation(projects.common.errorHandler.logic)
            implementation(projects.common.files)

            implementation(projects.data.common.token.logic)
            implementation(projects.domain.common.token.logic)

            implementation(projects.data.common.user.logic)
            implementation(projects.domain.common.user.logic)

            implementation(projects.domain.common.splash.logic)
            implementation(projects.domain.client.splash.logic)
            implementation(projects.features.client.splash)

            implementation(projects.data.client.auth.logic)
            implementation(projects.domain.client.auth.logic)
            implementation(projects.domain.common.auth.logic)
            implementation(projects.features.client.auth)

            implementation(projects.data.common.chat.logic)
            implementation(projects.data.client.chat.logic)
            implementation(projects.domain.common.chat.logic)
            implementation(projects.domain.client.chat.logic)
            implementation(projects.features.client.chat)

            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.material3.window.size)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)

            implementation(libs.decompose)
            implementation(libs.decompose.extensionsCompose)
            implementation(libs.essenty)

            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)

            implementation(libs.kotlinx.serializationJson)

            implementation(libs.koin.core)
            implementation(libs.filekit.dialogs)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "ru.kabanchik.client.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ru.kabanchik.client"
            packageVersion = "1.0.10"
            modules("jdk.unsupported", "jdk.security.auth")

            macOS {
                iconFile.set(project.file("src/jvmMain/resources/app-icon.icns"))
            }
            windows {
                iconFile.set(project.file("src/jvmMain/resources/app-icon.ico"))
            }
            linux {
                iconFile.set(project.file("src/jvmMain/resources/app-icon.png"))
            }
        }
    }
}
