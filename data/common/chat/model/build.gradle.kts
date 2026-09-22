plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "ru.kabanchik.common.data.chatDetails.model"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DataCommonChatDetailsModel"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serializationJson)
        }
    }
}
