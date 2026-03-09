package ru.kabanchik.pro.feature.chat.api.list

import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.pro.domain.chat.logic.api.ProChatsListInteractor

interface ProChatsListDependencies {
    val chatsListInteractor: ProChatsListInteractor
    val errorHandler: ErrorHandler

    class Factory(
        dependencies: ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowDependencies
    ) : ProChatsListDependencies, ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowDependencies by dependencies
}