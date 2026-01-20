plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.compose)
    alias(libs.plugins.build.decompose)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ChatDetails"
            isStatic = true
        }
    }

    sourceSets {

    }
}

android {
    namespace = "ru.kabanchik.client.chatDetails"
}

