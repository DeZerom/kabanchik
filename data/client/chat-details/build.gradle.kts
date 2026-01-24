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
            baseName = "DataChatDetails"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.network)

            implementation(projects.domain.client.chatDetails.logic)
        }
    }
}

android {
    namespace = "ru.kabanchik.client.data.chatDetails"
}

