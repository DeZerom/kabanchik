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
            baseName = "DomainLogicChatDetails"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.domain.client.chatDetails.model)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "ru.kabanchik.client.domain.logic.chatDetails"
}