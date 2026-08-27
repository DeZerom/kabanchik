package ru.kabanchik.client.di

import org.koin.core.module.Module
import ru.kabanchik.client.data.auth.logic.api.di.DataClientAuthModule
import ru.kabanchik.client.data.chat.logic.api.di.DataClientChatDetailsModule
import ru.kabanchik.client.data.token.logic.api.di.DataCommonTokenModule
import ru.kabanchik.client.domain.auth.logic.api.di.DomainClientAuthModule
import ru.kabanchik.client.domain.logic.chat.api.di.DomainClientChatDetailsModule
import ru.kabanchik.client.domain.splash.logic.api.di.DomainClientSplashModule
import ru.kabanchik.common.data.chat.logic.api.di.DataCommonChatModule
import ru.kabanchik.common.data.user.logic.api.di.DataCommonUserModule
import ru.kabanchik.common.datastore.api.di.CommonDataStoreModule
import ru.kabanchik.common.domain.auth.logic.api.di.DomainCommonAuthModule
import ru.kabanchik.common.domain.chat.logic.api.di.DomainCommonChatModule
import ru.kabanchik.common.domain.splash.logic.api.di.DomainCommonSplashModule
import ru.kabanchik.common.domain.user.logic.api.di.DomainCommonUserModule
import ru.kabanchik.common.errorHandler.logic.api.di.CommonErrorHandlerModule
import ru.kabanchik.common.files.api.di.CommonFilesModule
import ru.kabanchik.common.network.api.di.CommonNetworkModule

expect val platformModules: List<Module>

val commonModules = listOf(
    CommonNetworkModule.module,
    CommonDataStoreModule.module,
    CommonErrorHandlerModule.module,
    CommonFilesModule.module
)

val dataModules = listOf(
    DataClientChatDetailsModule.module,
    DataClientAuthModule.module,
    DataCommonTokenModule.module,
    DataCommonUserModule.module,
    DataCommonChatModule.module
)

val domainModules = listOf(
    DomainCommonSplashModule.module,
    DomainClientSplashModule.module,
    DomainCommonAuthModule.module,
    DomainClientAuthModule.module,
    DomainCommonUserModule.module,
    DomainCommonChatModule.module,
    DomainClientChatDetailsModule.module
)

fun clientAppModules(): List<Module> = platformModules + commonModules + domainModules + dataModules
