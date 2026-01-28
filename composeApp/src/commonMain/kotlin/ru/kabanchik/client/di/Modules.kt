package ru.kabanchik.client.di

import org.koin.core.module.Module
import ru.kabanchik.client.data.auth.logic.api.di.DataClientAuthModule
import ru.kabanchik.client.data.chatDetails.logic.api.di.DataClientChatDetailsModule
import ru.kabanchik.client.data.token.logic.api.di.DataClientTokenModule
import ru.kabanchik.client.domain.auth.logic.api.di.DomainClientAuthModule
import ru.kabanchik.client.domain.logic.chatDetails.api.di.DomainClientChatDetailsModule
import ru.kabanchik.common.datastore.api.di.CommonDataStoreModule
import ru.kabanchik.common.network.api.di.CommonNetworkModule

expect val platformModules: List<Module>

val commonModules = listOf(
    CommonNetworkModule.module,
    CommonDataStoreModule.module
)

val dataModules = listOf(
    DataClientChatDetailsModule.module,
    DataClientAuthModule.module,
    DataClientTokenModule.module
)

val domainModules = listOf(
    DomainClientChatDetailsModule.module,
    DomainClientAuthModule.module,
)

fun appModules(): List<Module> = platformModules + commonModules + domainModules + dataModules