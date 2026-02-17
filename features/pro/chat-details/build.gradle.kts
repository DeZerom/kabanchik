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
            baseName = "FeatureProChatDetails"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.pro.chatDetails.logic)
            implementation(projects.domain.common.user.logic)

            implementation(projects.common.uiKit)
            implementation(projects.common.store)
            implementation(projects.common.tools)

            implementation(libs.components.resources)
            implementation(libs.kotlinx.datetime)
        }
    }
}

android {
    namespace = "ru.kabanchik.pro.feature.chatDetails"
}

