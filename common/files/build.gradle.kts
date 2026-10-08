plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.koin)
}

kotlin {
    android {
        namespace = "ru.kabanchik.common.files"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.filekit.dialogs)
            implementation(libs.ktor.core)
            api(libs.kotlinx.io.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
