plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.compose)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "UiKit"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.components.resources)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.uiKit"
}

