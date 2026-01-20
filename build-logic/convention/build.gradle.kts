plugins {
    `kotlin-dsl`
}

group = "ru.kabanchik.app.buildLogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kmp") {
            id = "ru.kabanchik.app.buildLogic.kmp"
            implementationClass = "KotlinMultiplatformPlugin"
        }
    }
}