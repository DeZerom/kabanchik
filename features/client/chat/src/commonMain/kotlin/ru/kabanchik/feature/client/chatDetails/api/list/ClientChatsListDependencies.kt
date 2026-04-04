package ru.kabanchik.feature.client.chatDetails.api.list

import ru.kabanchik.client.domain.logic.chat.api.ClientChatsListInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.feature.client.chatDetails.api.flow.ClientChatFlowDependencies

interface ClientChatsListDependencies {
    val listInteractor: ClientChatsListInteractor
    val errorHandler: ErrorHandler

    class Factory(
        dependencies: ClientChatFlowDependencies
    ) : ClientChatsListDependencies, ClientChatFlowDependencies by dependencies
}