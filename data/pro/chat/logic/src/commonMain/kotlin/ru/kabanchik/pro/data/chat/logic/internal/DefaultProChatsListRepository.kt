package ru.kabanchik.pro.data.chat.logic.internal

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kabanchik.common.chat.model.CommonSystemMessage
import ru.kabanchik.common.data.chat.logic.api.toDomain
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatsListRepository
import ru.kabanchik.pro.data.chat.logic.api.ProMessagesStompSource
import ru.kabanchik.pro.data.chat.logic.internal.mappers.toDomain
import ru.kabanchik.pro.data.chatDetails.model.ProApiAcceptChat
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatsListRepository
import ru.kabanchik.pro.domain.chatDetails.model.ProIncoming

internal class DefaultProChatsListRepository(
    commonRepository: CommonChatsListRepository,
    private val stompSource: ProMessagesStompSource
) : ProChatsListRepository, CommonChatsListRepository by commonRepository {
    override suspend fun register() {
        stompSource.register()
    }

    override suspend fun listenIncoming(): Flow<ProIncoming> {
        return stompSource.listenIncoming().map { it.toDomain() }
    }

    override suspend fun acceptChat(clientLogin: String) {
        stompSource.acceptChat(
            message = ProApiAcceptChat(clientLogin)
        )
    }

    override suspend fun listenSystem(): Flow<CommonSystemMessage> {
        return stompSource.listenSystem().map { it.toDomain() }
    }
}