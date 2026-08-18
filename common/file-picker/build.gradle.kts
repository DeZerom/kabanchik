plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.koin)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.filekit.dialogs)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.filePicker"
}
