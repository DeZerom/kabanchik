package ru.kabanchik.feature.client.chatDetails.api.details

import ru.kabanchik.client.domain.logic.chat.api.ClientChatDetailsInteractor
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.feature.client.chatDetails.api.flow.ClientChatFlowDependencies

interface ClientChatDetailsDependencies {
    val chatDetailsInteractor: ClientChatDetailsInteractor
    val userInteractor: UserInteractor
    val errorHandler: ErrorHandler

    class Factory(
        dependencies: ClientChatFlowDependencies
    ) : ClientChatDetailsDependencies, ClientChatFlowDependencies by dependencies
}