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
            implementationClass = "ru.kabanchik.app.buildLogic.plugins.KotlinMultiplatformPlugin"
        }
        register("decomposePlugin") {
            id = "ru.kabanchik.app.buildLogic.decomposePlugin"
            implementationClass = "ru.kabanchik.app.buildLogic.plugins.DecomposePlugin"
        }
        register("composePlugin") {
            id = "ru.kabanchik.app.buildLogic.composePlugin"
            implementationClass = "ru.kabanchik.app.buildLogic.plugins.ComposePlugin"
        }
    }
}