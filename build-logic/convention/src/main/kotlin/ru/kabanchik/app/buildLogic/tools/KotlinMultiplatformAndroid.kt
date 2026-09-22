package ru.kabanchik.app.buildLogic.tools

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Доступ к `kotlin { android { } }` из convention-плагинов.
 *
 * Названо `androidLibrary`, а не `android`: у [KotlinMultiplatformExtension] есть deprecated
 * member-функция `android()` (target preset), которая перекрыла бы extension-функцию.
 */
internal fun KotlinMultiplatformExtension.androidLibrary(
    action: KotlinMultiplatformAndroidLibraryTarget.() -> Unit,
) {
    (this as ExtensionAware)
        .extensions
        .configure(KotlinMultiplatformAndroidLibraryTarget::class.java, action)
}
