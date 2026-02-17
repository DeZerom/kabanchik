package ru.kabanchik.pro.feature.chatDetails.api

import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.pro.domain.chatDetails.logic.api.ProChatDetailsInteractor

interface ProChatDetailsDependencies {
    val chatDetailsInteractor: ProChatDetailsInteractor
    val userInteractor: UserInteractor

    class Factory(
        override val chatDetailsInteractor: ProChatDetailsInteractor,
        override val userInteractor: UserInteractor
    ) : ProChatDetailsDependencies
}