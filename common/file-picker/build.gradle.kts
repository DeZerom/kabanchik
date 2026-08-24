plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.koin)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.filekit.dialogs)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.filePicker"
}
