package ru.kabanchik.feature.client.chatDetails.api.flow

import ru.kabanchik.client.domain.logic.chat.api.ClientChatDetailsInteractor
import ru.kabanchik.client.domain.logic.chat.api.ClientChatsListInteractor
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.files.api.FileOpener
import ru.kabanchik.common.files.api.FilePicker

interface ClientChatFlowDependencies {
    val listInteractor: ClientChatsListInteractor
    val chatDetailsInteractor: ClientChatDetailsInteractor
    val userInteractor: UserInteractor
    val errorHandler: ErrorHandler
    val filePicker: FilePicker
    val fileOpener: FileOpener

    class Factory(
        override val listInteractor: ClientChatsListInteractor,
        override val chatDetailsInteractor: ClientChatDetailsInteractor,
        override val userInteractor: UserInteractor,
        override val errorHandler: ErrorHandler,
        override val filePicker: FilePicker,
        override val fileOpener: FileOpener,
    ) : ClientChatFlowDependencies
}
