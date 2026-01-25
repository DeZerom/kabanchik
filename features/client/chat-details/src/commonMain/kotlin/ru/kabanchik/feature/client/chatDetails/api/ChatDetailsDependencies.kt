package ru.kabanchik.feature.client.chatDetails.api

import ru.kabanchik.client.domain.logic.chatDetails.api.ChatDetailsInteractor

interface ChatDetailsDependencies {
    val chatDetailsInteractor: ChatDetailsInteractor

    class Factory(
        override val chatDetailsInteractor: ChatDetailsInteractor
    ) : ChatDetailsDependencies
}