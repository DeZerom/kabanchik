package ru.kabanchik.pro.feature.chat.api.details

import ru.kabanchik.common.domain.user.logic.api.UserInteractor
import ru.kabanchik.common.errorHandler.logic.api.ErrorHandler
import ru.kabanchik.common.files.api.FileOpener
import ru.kabanchik.common.files.api.FilePicker
import ru.kabanchik.pro.domain.chat.logic.api.ProChatDetailsInteractor

interface ProChatDetailsDependencies {
    val chatDetailsInteractor: ProChatDetailsInteractor
    val userInteractor: UserInteractor
    val errorHandler: ErrorHandler
    val filePicker: FilePicker
    val fileOpener: FileOpener

    class Factory(
        dependencies: ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowDependencies
    ) : ProChatDetailsDependencies, ru.kabanchik.pro.feature.chat.api.flow.ProChatFlowDependencies by dependencies
}
