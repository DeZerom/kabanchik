package ru.kabanchik.client.feature.auth.internal.register

import com.arkivanov.decompose.ComponentContext
import ru.kabanchik.client.feature.auth.api.register.RegisterComponent

internal class DefaultRegisterComponent(
    componentContext: ComponentContext
) : RegisterComponent, ComponentContext by componentContext