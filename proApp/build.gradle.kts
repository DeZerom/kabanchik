import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    jvm()

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ProApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.data.common.token.logic)
            implementation(projects.domain.common.token.logic)

            implementation(projects.domain.common.user.logic)
            implementation(projects.data.common.user.logic)

            implementation(projects.domain.common.splash.logic)
            implementation(projects.domain.pro.splash.logic)
            implementation(projects.features.pro.splash)

            implementation(projects.data.pro.auth.logic)
            implementation(projects.domain.common.auth.logic)
            implementation(projects.domain.pro.auth.logic)
            implementation(projects.features.pro.auth)

            implementation(projects.data.common.chat.logic)
            implementation(projects.data.pro.chat.logic)
            implementation(projects.domain.common.chat.logic)
            implementation(projects.domain.pro.chat.logic)
            implementation(projects.features.pro.chat)

            implementation(projects.common.uiKit)
            implementation(projects.common.network)
            implementation(projects.common.dataStore)
            implementation(projects.common.tools)
            implementation(projects.common.errorHandler.logic)
            implementation(projects.common.files)

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
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.androidx.activity.compose)
        }
    }
}

compose.desktop {
    application {
        mainClass = "ru.kabanchik.pro.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "ru.kabanchik.pro"
            packageVersion = "1.0.5"
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

android {
    namespace = "ru.kabanchik.pro"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.kabanchik.pro"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 6
        versionName = "0.0.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}
