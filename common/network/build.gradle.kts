plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.koin)
    alias(libs.plugins.kotlinx.serialization)
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
            implementation(libs.kotlinx.serializationJson)
            implementation(libs.krossbow.core)
            implementation(libs.krossbow.ktor)
            implementation(libs.krossbow.serialization.json)

            implementation(projects.data.client.chatDetails.logic)
            implementation(projects.data.client.auth.logic)
        }
        jvmMain.dependencies {
            implementation(libs.logback)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.network"
}

