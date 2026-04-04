plugins {
    alias(libs.plugins.build.kmp)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DomainCommonTokenModel"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
        }
    }
}

android {
    namespace = "ru.kabanchik.common.token.model"
}

