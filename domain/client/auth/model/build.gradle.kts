plugins {
    alias(libs.plugins.build.kmp)
}

kotlin {
    android {
        namespace = "ru.kabanchik.client.domain.token.model"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DomainClientTokenModel"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
        }
    }
}
