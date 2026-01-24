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
        commonMain.dependencies {
            implementation(projects.common.uiKit)
            implementation(projects.common.store)

            implementation(libs.components.resources)
        }
    }
}

android {
    namespace = "ru.kabanchik.client.chatDetails"
}

