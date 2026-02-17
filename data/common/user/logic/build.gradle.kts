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
            baseName = "DataCommonUserLogic"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.common.user.logic)

            implementation(projects.common.dataStore)

            implementation(libs.kotlinx.serializationJson)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.data.user.logic"
}

