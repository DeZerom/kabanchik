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
            baseName = "FeatureProChat"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.common.chat.logic)
            implementation(projects.domain.pro.chat.logic)
            implementation(projects.features.common.chat.logic)
            implementation(projects.features.common.imageViewer)
            implementation(projects.domain.common.user.logic)

            implementation(projects.common.uiKit)
            implementation(projects.common.store)
            implementation(projects.common.tools)
            implementation(projects.common.errorHandler.logic)
            implementation(projects.common.files)

            implementation(libs.components.resources)
            implementation(libs.kotlinx.datetime)
        }
        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "ru.kabanchik.pro.feature.chat"
}
