plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "ru.kabanchik.pro"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.kabanchik.pro"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 6
        versionName = "0.0.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(projects.proApp)
    implementation(projects.common.uiKit)

    implementation(libs.androidx.activity.compose)
    implementation(libs.runtime)
    implementation(libs.material3.window.size)
    implementation(libs.decompose)
    implementation(libs.koin.android)
    implementation(libs.filekit.dialogs)

    debugImplementation(libs.ui.tooling)
}
