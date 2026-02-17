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
            baseName = "DataCommonTokenLogic"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.common.token.logic)
            implementation(projects.domain.client.auth.logic)
            implementation(projects.domain.pro.auth.logic)
            implementation(projects.domain.pro.chatDetails.logic)

            implementation(projects.common.dataStore)

            implementation(libs.kotlinx.serializationJson)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.data.token.logic"
}

