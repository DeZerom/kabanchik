plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.koin)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DataProChatDetailsLogic"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.data.pro.chatDetails.model)

            implementation(projects.domain.pro.chatDetails.logic)

            implementation(libs.kotlinx.serializationJson)
        }
    }
}

android {
    namespace = "ru.kabanchik.pro.data.chatDetails.logic"
}

