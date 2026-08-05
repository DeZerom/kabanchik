package ru.kabanchik.client.data.chat.logic.internal

import ru.kabanchik.client.domain.logic.chat.api.repository.ClientChatDetailsRepository
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatDetailsRepository

internal class DefaultClientChatDetailsRepository(
    commonRepository: CommonChatDetailsRepository
) : ClientChatDetailsRepository, CommonChatDetailsRepository by commonRepository
