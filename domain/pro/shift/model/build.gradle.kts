plugins {
    alias(libs.plugins.build.kmp)
}

kotlin {
    android {
        namespace = "ru.kabanchik.pro.domain.shift.model"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DomainProShiftModel"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {

        }
    }
}
