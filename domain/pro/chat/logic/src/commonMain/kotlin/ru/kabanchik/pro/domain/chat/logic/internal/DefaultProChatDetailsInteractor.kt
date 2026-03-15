package ru.kabanchik.pro.domain.chat.logic.internal

import ru.kabanchik.common.domain.chat.logic.api.CommonChatDetailsInteractor
import ru.kabanchik.pro.domain.chat.logic.api.ProChatDetailsInteractor

internal class DefaultProChatDetailsInteractor(
    commonInteractor: CommonChatDetailsInteractor
) : ProChatDetailsInteractor, CommonChatDetailsInteractor by commonInteractor {
}