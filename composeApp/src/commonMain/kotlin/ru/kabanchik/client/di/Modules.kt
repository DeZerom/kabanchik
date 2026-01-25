package ru.kabanchik.client.di

import org.koin.core.module.Module
import ru.kabanchik.client.data.chatDetails.logic.api.di.DataClientChatDetailsModule
import ru.kabanchik.client.domain.logic.chatDetails.api.di.DomainClientChatDetails
import ru.kabanchik.common.network.api.di.CommonNetworkModule

val commonModules = listOf(
    CommonNetworkModule.module
)

val dataModules = listOf(
    DataClientChatDetailsModule.module
)

val domainModules = listOf(
    DomainClientChatDetails.module
)

fun appModules(): List<Module> = commonModules + domainModules + dataModules