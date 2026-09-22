plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.compose)
}

kotlin {
    android {
        namespace = "ru.kabanchik.common.tools"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CommonTools"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
        }
    }
}
