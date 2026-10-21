plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.koin)
}

kotlin {
    android {
        namespace = "ru.kabanchik.pro.domain.shift.logic"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "DomainProShiftLogic"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.domain.pro.shift.model)
            implementation(projects.domain.common.profile.logic)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
