plugins {
    alias(libs.plugins.build.kmp)
}

kotlin {
    android {
        namespace = "ru.kabanchik.common.chat.model"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DomainCommonChatModel"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.datetime)
        }
    }
}
