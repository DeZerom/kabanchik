plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DataChatDetailsModel"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serializationJson)
        }
    }
}

android {
    namespace = "ru.kabanchik.client.data.chatDetails.model"
}

