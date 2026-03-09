package ru.kabanchik.pro.feature.chatDetails.api.details

import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.pro.domain.chatDetails.logic.api.ProChatDetailsInteractor
import ru.kabanchik.pro.feature.chatDetails.api.flow.ProChatFlowDependencies

interface ProChatDetailsDependencies {
    val chatDetailsInteractor: ProChatDetailsInteractor
    val userInteractor: UserInteractor
    val errorHandler: ErrorHandler

    class Factory(
        dependencies: ProChatFlowDependencies
    ) : ProChatDetailsDependencies, ProChatFlowDependencies by dependencies
}