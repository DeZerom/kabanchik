plugins {
    alias(libs.plugins.build.kmp)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CommonNetwork"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.core)
            implementation(libs.ktor.cio)
            implementation(libs.ktor.content.negotiaion)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.websocket)
            implementation(libs.ktor.auth)
        }
        jvmMain.dependencies {
            implementation(libs.logback)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.network"
}

