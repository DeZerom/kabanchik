plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.compose)
}

kotlin {
    android {
        namespace = "ru.kabanchik.common.feature.profile"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FeatureCommonProfile"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.uiKit)
            implementation(projects.common.tools)
        }
    }
}
