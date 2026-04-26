package ru.kabanchik.common.scaffold

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kabanchik.common.scaffold.toolbar.CommonToolbarModel

object ScaffoldState {
    private val _toolbar: MutableStateFlow<CommonToolbarModel?> = MutableStateFlow(null)
    val toolbar: StateFlow<CommonToolbarModel?> = _toolbar.asStateFlow()

    fun setToolbar(toolbarModel: CommonToolbarModel?) {
        _toolbar.value = toolbarModel
    }
}