plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.decompose)
}

kotlin {
    android {
        namespace = "ru.kabanchik.common.store"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CommonStore"
            isStatic = true
        }
    }

    sourceSets {
    }
}
