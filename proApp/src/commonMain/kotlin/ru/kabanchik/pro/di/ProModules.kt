package ru.kabanchik.pro.di

import org.koin.core.module.Module

expect val platformModules: List<Module>

fun proAppModules(): List<Module> {
    return platformModules
}