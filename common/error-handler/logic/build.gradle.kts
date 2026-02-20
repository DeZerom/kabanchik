plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.koin)
    alias(libs.plugins.build.compose)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CommonErrorHandlerLogic"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.tools)

            implementation(libs.ktor.core)
            implementation(libs.components.resources)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.errorHandler.logic"
}