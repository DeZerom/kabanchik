package ru.kabanchik.pro.feature.chat.api.flow

import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.pro.domain.chat.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.domain.chat.logic.api.ProChatsListInteractor

interface ProChatFlowDependencies {
    val chatsListInteractor: ProChatsListInteractor
    val chatDetailsInteractor: ProChatDetailsInteractor
    val userInteractor: UserInteractor
    val errorHandler: ErrorHandler

    class Factory(
        override val chatsListInteractor: ProChatsListInteractor,
        override val chatDetailsInteractor: ProChatDetailsInteractor,
        override val userInteractor: UserInteractor,
        override val errorHandler: ErrorHandler
    ) : ProChatFlowDependencies
}