plugins {
    alias(libs.plugins.build.kmp)
}

kotlin {
    android {
        namespace = "ru.kabanchik.client.domain.model.chatDetails"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DomainModelChatDetails"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
        }
    }
}
