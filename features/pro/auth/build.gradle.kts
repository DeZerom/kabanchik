plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.compose)
    alias(libs.plugins.build.decompose)
}

kotlin {
    android {
        namespace = "ru.kabanchik.pro.feature.auth"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FeatureProAuth"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.pro.auth.logic)
            implementation(projects.features.common.auth.logic)

            implementation(projects.common.uiKit)
            implementation(projects.common.store)
            implementation(projects.common.tools)
            implementation(projects.common.errorHandler.logic)

            implementation(libs.components.resources)
        }
    }
}
