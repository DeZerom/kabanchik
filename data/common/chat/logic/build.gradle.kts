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
            baseName = "DataCommonChatLogic"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.data.common.chat.model)
            api(projects.common.files)
            implementation(projects.domain.common.chat.logic)

            implementation(libs.kotlinx.datetime)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.data.chat.logic"
}
