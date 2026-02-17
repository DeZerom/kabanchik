package ru.kabanchik.pro.di

import org.koin.core.module.Module
import ru.kabanchik.client.data.token.logic.api.di.DataCommonTokenModule
import ru.kabanchik.common.data.user.logic.api.di.DataCommonUserModule
import ru.kabanchik.common.datastore.api.di.CommonDataStoreModule
import ru.kabanchik.common.domain.user.logic.api.di.DomainCommonUserModule
import ru.kabanchik.common.network.api.di.CommonNetworkModule
import ru.kabanchik.pro.data.auth.logic.api.di.DataProAuthModule
import ru.kabanchik.pro.data.chatDetails.logic.api.di.DataProChatDetailsModule
import ru.kabanchik.pro.domain.auth.logic.api.di.DomainProAuthDiModule
import ru.kabanchik.pro.domain.chatDetails.logic.api.di.DomainProChatDetailsModule

expect val platformModules: List<Module>

private val commonModules = listOf(
    CommonDataStoreModule.module,
    CommonNetworkModule.module
)

private val domainModules = listOf(
    DomainProAuthDiModule.module,
    DomainCommonUserModule.module,
    DomainProChatDetailsModule.module
)

private val dataModules = listOf(
    DataProAuthModule.module,
    DataCommonTokenModule.module,
    DataCommonUserModule.module,
    DataProChatDetailsModule.module
)

fun proAppModules(): List<Module> {
    return platformModules + commonModules + domainModules + dataModules
}