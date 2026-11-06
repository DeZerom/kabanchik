package ru.kabanchik.common.files.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.files.api.FileOpener
import ru.kabanchik.common.files.api.FilePicker
import ru.kabanchik.common.files.internal.DefaultFileOpener
import ru.kabanchik.common.files.internal.DefaultFilePicker

object CommonFilesModule {
    val module = module {
        singleOf(::DefaultFilePicker) bind FilePicker::class
        singleOf(::DefaultFileOpener) bind FileOpener::class
    }
}
