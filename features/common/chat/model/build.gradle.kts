plugins {
    alias(libs.plugins.build.kmp)
}

kotlin {
    android {
        namespace = "ru.kabanchik.common.feature.chat.model"
    }

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
            api(projects.common.files)
            implementation(projects.common.tools)
        }
    }
}
