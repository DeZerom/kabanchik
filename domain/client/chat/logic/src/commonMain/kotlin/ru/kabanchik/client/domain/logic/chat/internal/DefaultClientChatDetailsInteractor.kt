package ru.kabanchik.client.domain.logic.chat.internal

import ru.kabanchik.client.domain.logic.chat.api.ClientChatDetailsInteractor
import ru.kabanchik.common.domain.chat.logic.api.CommonChatDetailsInteractor

class DefaultClientChatDetailsInteractor(
    commonInteractor: CommonChatDetailsInteractor
) : ClientChatDetailsInteractor, CommonChatDetailsInteractor by commonInteractor