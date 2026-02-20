package ru.kabanchik.common.errorHandler.logic.api.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.errorHandler.logic.internal.DefaultErrorHandler

object CommonErrorHandlerModule {
    val module = module {
        singleOf(::DefaultErrorHandler) bind ErrorHandler::class
    }
}