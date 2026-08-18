plugins {
    alias(libs.plugins.build.kmp)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FeatureCommonChatModel"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.filePicker)
            implementation(projects.common.tools)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.feature.chat.model"
}
