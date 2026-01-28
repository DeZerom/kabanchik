package ru.kabanchik.client.feature.auth.internal.auth

import com.arkivanov.decompose.ComponentContext
import ru.kabanchik.client.feature.auth.api.auth.AuthComponent

internal class DefaultAuthComponent(
    componentContext: ComponentContext
) : AuthComponent, ComponentContext by componentContext