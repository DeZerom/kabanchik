package mock

import ru.kabanchik.common.domain.profile.logic.api.repository.CommonProfileUserRepository

class MockCommonProfileUserRepository(
    private val login: String?
) : CommonProfileUserRepository {
    val calls = mutableListOf<String>()

    override suspend fun getUserLogin(): String? {
        return login
    }

    override suspend fun disconnect() {
        calls += Disconnect
    }

    override suspend fun clearLocalData() {
        calls += ClearLocalData
    }

    companion object {
        const val ClearLocalData = "clearLocalData"
        const val Disconnect = "disconnect"
    }
}
