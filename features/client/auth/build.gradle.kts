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
            baseName = "FeatureAuth"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.client.auth.logic)

            implementation(projects.common.uiKit)
            implementation(projects.common.store)
            implementation(projects.common.tools)

            implementation(libs.components.resources)
        }
    }
}

android {
    namespace = "ru.kabanchik.client.feature.auth"
}

