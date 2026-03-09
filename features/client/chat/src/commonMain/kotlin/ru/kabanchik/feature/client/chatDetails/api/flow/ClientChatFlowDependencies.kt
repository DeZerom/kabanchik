package ru.kabanchik.feature.client.chatDetails.api.flow

import ru.kabanchik.client.domain.logic.chat.api.ChatDetailsInteractor
import ru.kabanchik.client.domain.logic.chat.api.ClientChatsListInteractor
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler

interface ClientChatFlowDependencies {
    val listInteractor: ClientChatsListInteractor
    val chatDetailsInteractor: ChatDetailsInteractor
    val userInteractor: UserInteractor
    val errorHandler: ErrorHandler

    class Factory(
        override val listInteractor: ClientChatsListInteractor,
        override val chatDetailsInteractor: ChatDetailsInteractor,
        override val userInteractor: UserInteractor,
        override val errorHandler: ErrorHandler
    ) : ClientChatFlowDependencies
}