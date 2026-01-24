package ru.kabanchik.client.di

import org.koin.core.module.Module
import ru.kabanchik.client.data.chatDetails.api.di.DataClientChatDetailsModule
import ru.kabanchik.common.network.api.di.CommonNetworkModule

val commonModules = listOf(
    CommonNetworkModule.module
)

val dataModules = listOf(
    DataClientChatDetailsModule.module
)

fun appModules(): List<Module> = commonModules + dataModules