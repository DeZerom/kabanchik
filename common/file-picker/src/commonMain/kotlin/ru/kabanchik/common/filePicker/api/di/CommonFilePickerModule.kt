package ru.kabanchik.common.filePicker.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.filePicker.api.FilePicker
import ru.kabanchik.common.filePicker.internal.DefaultFilePicker

object CommonFilePickerModule {
    val module = module {
        singleOf(::DefaultFilePicker) bind FilePicker::class
    }
}
