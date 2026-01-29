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
            baseName = "DataClientAuthLogic"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.data.client.auth.model)
            implementation(projects.domain.client.auth.logic)

            implementation(libs.kotlinx.serializationJson)
        }
    }
}

android {
    namespace = "ru.kabanchik.client.data.auth.logic"
}

