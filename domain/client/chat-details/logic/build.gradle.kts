plugins {
    alias(libs.plugins.build.kmp)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DomainLogicChatDetails"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.domain.client.chatDetails.model)
        }
    }
}

android {
    namespace = "ru.kabanchik.client.domain.logic.chatDetails"
}

