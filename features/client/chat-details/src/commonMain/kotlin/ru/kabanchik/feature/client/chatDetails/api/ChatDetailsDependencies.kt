package ru.kabanchik.feature.client.chatDetails.api

import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsInteractor
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler

interface ChatDetailsDependencies {
    val chatDetailsInteractor: ChatDetailsInteractor
    val userInteractor: UserInteractor
    val errorHandler: ErrorHandler

    class Factory(
        override val chatDetailsInteractor: ChatDetailsInteractor,
        override val userInteractor: UserInteractor,
        override val errorHandler: ErrorHandler
    ) : ChatDetailsDependencies
}