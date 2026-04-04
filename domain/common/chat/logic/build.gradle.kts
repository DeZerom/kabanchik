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
            baseName = "DomainCommonChatLogic"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.domain.common.chat.model)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.domain.chat.logic"
}

