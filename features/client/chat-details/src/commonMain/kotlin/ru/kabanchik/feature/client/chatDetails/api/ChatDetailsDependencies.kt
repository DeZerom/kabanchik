package ru.kabanchik.feature.client.chatDetails.api

import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsInteractor
import ru.kabanchik.common.domain.user.logic.api.UserInteractor

interface ChatDetailsDependencies {
    val chatDetailsInteractor: ChatDetailsInteractor
    val userInteractor: UserInteractor

    class Factory(
        override val chatDetailsInteractor: ChatDetailsInteractor,
        override val userInteractor: UserInteractor
    ) : ChatDetailsDependencies
}