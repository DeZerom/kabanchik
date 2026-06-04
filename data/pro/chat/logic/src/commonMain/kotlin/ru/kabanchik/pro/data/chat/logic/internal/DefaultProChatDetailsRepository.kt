package ru.kabanchik.pro.data.chat.logic.internal

import ru.kabanchik.common.domain.chat.logic.api.repository.CommonChatDetailsRepository
import ru.kabanchik.pro.domain.chat.logic.api.repository.ProChatDetailsRepository

internal class DefaultProChatDetailsRepository(
    commonRepository: CommonChatDetailsRepository
) : ProChatDetailsRepository, CommonChatDetailsRepository by commonRepository
