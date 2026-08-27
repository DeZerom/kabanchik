plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.compose)
    alias(libs.plugins.build.decompose)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FeatureCommonImageViewer"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.common.uiKit)
            implementation(projects.common.tools)

            implementation(libs.coil.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "ru.kabanchik.common.feature.imageViewer"
}
