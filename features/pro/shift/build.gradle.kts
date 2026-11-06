plugins {
    alias(libs.plugins.build.kmp)
    alias(libs.plugins.build.compose)
    alias(libs.plugins.build.decompose)
}

kotlin {
    android {
        namespace = "ru.kabanchik.pro.feature.shift"
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FeatureProShift"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.pro.shift.logic)
            implementation(projects.features.common.profile)

            implementation(projects.common.uiKit)
            implementation(projects.common.store)
            implementation(projects.common.tools)
            implementation(projects.common.errorHandler.logic)
        }
        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
