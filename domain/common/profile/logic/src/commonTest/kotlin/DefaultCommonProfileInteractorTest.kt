import kotlinx.coroutines.runBlocking
import mock.MockCommonProfileUserRepository
import ru.kabanchik.common.domain.profile.logic.internal.DefaultCommonProfileInteractor
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultCommonProfileInteractorTest {
    @Test
    fun returnsSavedUserLogin() {
        runBlocking {
            val repository = MockCommonProfileUserRepository(login = "user")
            val interactor = DefaultCommonProfileInteractor(userRepository = repository)

            assertEquals("user", interactor.getUserLogin())
        }
    }

    @Test
    fun disconnectsBeforeClearingLocalDataOnLogout() {
        runBlocking {
            val repository = MockCommonProfileUserRepository(login = "user")
            val interactor = DefaultCommonProfileInteractor(userRepository = repository)

            interactor.logout()

            assertEquals(
                listOf(MockCommonProfileUserRepository.Disconnect, MockCommonProfileUserRepository.ClearLocalData),
                repository.calls
            )
        }
    }
}
