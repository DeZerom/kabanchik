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
            baseName = "FeatureClientSplash"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.features.common.splash.logic)
            implementation(projects.domain.client.splash.logic)

            implementation(projects.common.uiKit)
            implementation(projects.common.store)
            implementation(projects.common.tools)
            implementation(projects.common.errorHandler.logic)
        }
    }
}

android {
    namespace = "ru.kabanchik.client.feature.splash"
}

