package ru.kabanchik.pro.di

import org.koin.core.module.Module
import ru.kabanchik.client.data.token.logic.api.di.DataCommonTokenModule
import ru.kabanchik.common.datastore.api.di.CommonDataStoreModule
import ru.kabanchik.common.network.api.di.CommonNetworkModule
import ru.kabanchik.pro.data.auth.logic.api.di.DataProAuthModule
import ru.kabanchik.pro.domain.auth.logic.api.di.DomainProAuthDiModule

expect val platformModules: List<Module>

private val commonModules = listOf(
    CommonDataStoreModule.module,
    CommonNetworkModule.module
)

private val domainModules = listOf(
    DomainProAuthDiModule.module
)

private val dataModules = listOf(
    DataProAuthModule.module,
    DataCommonTokenModule.module
)

fun proAppModules(): List<Module> {
    return platformModules + commonModules + domainModules + dataModules
}