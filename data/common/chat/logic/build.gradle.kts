plugins {
    alias(libs.plugins.build.kmp)
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

            implementation(libs.kotlinx.datetime)
            implementation(projects.domain.common.chat.model)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.data.chat.logic"
}

