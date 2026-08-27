package ru.kabanchik.feature.client.chatDetails.internal.details

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.kabanchik.client.domain.logic.chat.api.ClientChatDetailsInteractor
import ru.kabanchik.common.chat.model.CommonAttachment
import ru.kabanchik.common.chat.model.CommonChatMessage
import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.errorHandler.logic.api.ErrorType
import ru.kabanchik.common.files.api.FileOpener

internal class FakeClientChatDetailsInteractor(
    private val upload: suspend () -> CommonAttachment,
    private val messages: List<CommonChatMessage> = emptyList(),
) : ClientChatDetailsInteractor {
    override suspend fun reconnect(sessionId: String): Unit = Unit

    override suspend fun getMessages(sessionId: String): List<CommonChatMessage> {
        return messages
    }

    override suspend fun uploadFile(
        sessionId: String,
        fileName: String,
        contentType: String,
        bytes: ByteArray,
    ): CommonAttachment {
        return upload()
    }

    override suspend fun downloadFile(sessionId: String, fileId: String): ByteArray {
        return byteArrayOf()
    }

    override suspend fun sendMessage(
        sessionId: String,
        content: String?,
        attachmentIds: List<String>,
    ): Unit = Unit

    override suspend fun listenMessages(sessionId: String): Flow<CommonChatMessage> {
        return emptyFlow()
    }

    override suspend fun endChat(sessionId: String): Unit = Unit
}

internal class FakeFileOpener(
    private val openFile: suspend (url: String, fileName: String) -> Unit = { _, _ -> },
) : FileOpener {
    override suspend fun open(url: String, fileName: String) {
        openFile(url, fileName)
    }
}

internal object FakeUserInteractor : UserInteractor {
    override suspend fun getUserLogin(): String? = null

    override suspend fun setUserLogin(login: String): Unit = Unit
}

internal object FakeErrorHandler : ErrorHandler {
    override fun handleError(error: Throwable): ErrorType = ErrorType.Unknown
}
