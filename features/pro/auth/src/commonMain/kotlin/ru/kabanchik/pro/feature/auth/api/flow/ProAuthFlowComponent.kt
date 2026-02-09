package ru.kabanchik.pro.feature.auth.api.flow

import com.arkivanov.decompose.ComponentContext

interface ProAuthFlowComponent {
    companion object {
        fun create(
            componentContext: ComponentContext,
            dependencies: ProAuthFlowDependencies
        ): ProAuthFlowComponent {

        }
    }
}