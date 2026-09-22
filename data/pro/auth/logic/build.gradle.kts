plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.koin)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "ru.kabanchik.pro.data.auth.logic"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DataProAuthLogic"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.data.pro.auth.model)
            implementation(projects.domain.pro.auth.logic)

            implementation(libs.kotlinx.serializationJson)
        }
    }
}
