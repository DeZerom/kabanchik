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
            baseName = "DomainProChatDetailsLogic"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.domain.pro.chatDetails.model)

            implementation(projects.domain.common.chatDetails.logic)
        }
    }
}

android {
    namespace = "ru.kabanchik.pro.domain.chatDetails.logic"
}