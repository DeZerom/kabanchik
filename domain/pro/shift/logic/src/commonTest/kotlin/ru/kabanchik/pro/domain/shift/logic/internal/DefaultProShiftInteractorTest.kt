package ru.kabanchik.pro.domain.shift.logic.internal

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import ru.kabanchik.common.domain.profile.logic.api.CommonProfileInteractor
import ru.kabanchik.pro.domain.shift.logic.api.repository.ProShiftRepository
import ru.kabanchik.pro.domain.shift.model.ProShiftStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class DefaultProShiftInteractorTest {
    @Test
    fun publishesStatusFromServer() {
        runBlocking {
            val repository = FakeProShiftRepository(serverStatus = ProShiftStatus.OffShift)
            val interactor = createInteractor(repository)

            assertEquals(ProShiftStatus.OffShift, interactor.refresh())
            assertEquals(ProShiftStatus.OffShift, interactor.shiftStatus.value)

            assertEquals(ProShiftStatus.OnShift, interactor.startShift())
            assertEquals(ProShiftStatus.OnShift, interactor.shiftStatus.value)

            assertEquals(ProShiftStatus.OffShift, interactor.endShift())
            assertEquals(ProShiftStatus.OffShift, interactor.shiftStatus.value)
        }
    }

    @Test
    fun treatsFailedEndAsSuccessWhenServerIsAlreadyOffShift() {
        runBlocking {
            val repository = FakeProShiftRepository(serverStatus = ProShiftStatus.OnShift).apply {
                endError = IllegalStateException("500")
                statusAfterFailedEnd = ProShiftStatus.OffShift
            }
            val interactor = createInteractor(repository)

            assertEquals(ProShiftStatus.OffShift, interactor.endShift())
            assertEquals(ProShiftStatus.OffShift, interactor.shiftStatus.value)
            assertEquals(listOf("endShift", "getShift"), repository.calls)
        }
    }

    @Test
    fun rethrowsEndErrorWhenServerIsStillOnShift() {
        runBlocking {
            val error = IllegalStateException("500")
            val repository = FakeProShiftRepository(serverStatus = ProShiftStatus.OnShift).apply {
                setShiftStatus(ProShiftStatus.OnShift)
                endError = error
            }
            val interactor = createInteractor(repository)

            val thrown = assertFailsWith<IllegalStateException> { interactor.endShift() }
            assertSame(error, thrown)
            assertEquals(ProShiftStatus.OnShift, interactor.shiftStatus.value)
        }
    }

    @Test
    fun rethrowsEndErrorWhenStatusCheckFails() {
        runBlocking {
            val error = IllegalStateException("500")
            val repository = FakeProShiftRepository(serverStatus = ProShiftStatus.OnShift).apply {
                endError = error
                getError = IllegalStateException("network")
            }
            val interactor = createInteractor(repository)

            val thrown = assertFailsWith<IllegalStateException> { interactor.endShift() }
            assertSame(error, thrown)
        }
    }

    @Test
    fun endsShiftBeforeLogoutAndPublishesStatusOnlyAfterLogout() {
        runBlocking {
            val repository = FakeProShiftRepository(serverStatus = ProShiftStatus.OnShift).apply {
                setShiftStatus(ProShiftStatus.OnShift)
            }
            val profileInteractor = FakeProfileInteractor(
                onLogout = { assertEquals(ProShiftStatus.OnShift, repository.shiftStatus.value) }
            )
            val interactor = createInteractor(repository, profileInteractor)

            interactor.logout()

            assertEquals(listOf("endShift"), repository.calls)
            assertEquals(1, profileInteractor.logoutCalls)
            assertNull(interactor.shiftStatus.value)
        }
    }

    @Test
    fun logsOutWithoutEndingShiftWhenOffShift() {
        runBlocking {
            val repository = FakeProShiftRepository(serverStatus = ProShiftStatus.OffShift).apply {
                setShiftStatus(ProShiftStatus.OffShift)
            }
            val profileInteractor = FakeProfileInteractor()
            val interactor = createInteractor(repository, profileInteractor)

            interactor.logout()

            assertEquals(emptyList(), repository.calls)
            assertEquals(1, profileInteractor.logoutCalls)
        }
    }

    @Test
    fun doesNotLogoutWhenShiftEndFails() {
        runBlocking {
            val repository = FakeProShiftRepository(serverStatus = ProShiftStatus.OnShift).apply {
                setShiftStatus(ProShiftStatus.OnShift)
                endError = IllegalStateException("500")
            }
            val profileInteractor = FakeProfileInteractor()
            val interactor = createInteractor(repository, profileInteractor)

            assertFailsWith<IllegalStateException> { interactor.logout() }

            assertEquals(0, profileInteractor.logoutCalls)
            assertEquals(ProShiftStatus.OnShift, interactor.shiftStatus.value)
        }
    }

    private fun createInteractor(
        repository: FakeProShiftRepository,
        profileInteractor: FakeProfileInteractor = FakeProfileInteractor()
    ): DefaultProShiftInteractor {
        return DefaultProShiftInteractor(
            shiftRepository = repository,
            profileInteractor = profileInteractor
        )
    }
}

private class FakeProShiftRepository(
    private var serverStatus: ProShiftStatus
) : ProShiftRepository {
    val calls = mutableListOf<String>()
    var endError: Throwable? = null
    var getError: Throwable? = null
    var statusAfterFailedEnd: ProShiftStatus? = null

    private val _shiftStatus = MutableStateFlow<ProShiftStatus?>(null)
    override val shiftStatus: StateFlow<ProShiftStatus?> = _shiftStatus

    override fun setShiftStatus(status: ProShiftStatus?) {
        _shiftStatus.value = status
    }

    override suspend fun getShift(): ProShiftStatus {
        calls += "getShift"
        getError?.let { throw it }
        return serverStatus
    }

    override suspend fun startShift(): ProShiftStatus {
        calls += "startShift"
        serverStatus = ProShiftStatus.OnShift
        return serverStatus
    }

    override suspend fun endShift(): ProShiftStatus {
        calls += "endShift"
        endError?.let { error ->
            statusAfterFailedEnd?.let { serverStatus = it }
            throw error
        }
        serverStatus = ProShiftStatus.OffShift
        return serverStatus
    }
}

private class FakeProfileInteractor(
    private val onLogout: () -> Unit = {}
) : CommonProfileInteractor {
    var logoutCalls = 0
        private set

    override suspend fun getUserLogin(): String? = null

    override suspend fun logout() {
        logoutCalls++
        onLogout()
    }
}
