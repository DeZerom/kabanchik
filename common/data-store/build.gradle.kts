plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.koin)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CommonDatastore"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.datastore"
}

