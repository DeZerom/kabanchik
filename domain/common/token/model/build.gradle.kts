plugins {
    alias(libs.plugins.build.kmp)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DomainCommonAuthModel"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
        }
    }
}

android {
    namespace = "ru.kabanchik.client.common.auth.model"
}

