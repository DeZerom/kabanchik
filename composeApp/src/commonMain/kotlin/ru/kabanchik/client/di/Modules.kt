package ru.kabanchik.client.di

import org.koin.core.module.Module
import ru.kabanchik.common.network.api.di.CommonNetworkModule

val commonModules = listOf(
    CommonNetworkModule.module
)

fun appModules(): List<Module> = commonModules