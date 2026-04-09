package mock

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonSessionMessage
import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatDetailsRepository

class MockCommonChatDetailsRepository(
    private val messages: List<CommonMessage>
) : CommonChatDetailsRepository {
    override suspend fun sendMessage(message: String) {
        TODO("Not yet implemented")
    }

    override suspend fun listenSession(): Flow<CommonSessionMessage> {
        return emptyFlow()
    }

    override suspend fun listenMessages(): Flow<CommonMessage> {
        return flow {
            messages.forEach { message -> emit(message) }
        }
    }

    override suspend fun endChat() {
        TODO("Not yet implemented")
    }

    override suspend fun listenSessionEnd(): Flow<CommonMessage> {
        return emptyFlow()
    }
}