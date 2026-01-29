package ru.kabanchik.common.snackBar.api

import ru.kabanchik.common.tools.textResource.TextResource

sealed interface SnackBarData {
    class Error(val text: TextResource) : SnackBarData
}