package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.headlineSmall

/**
 * Модальный bottom sheet приложения. Отвечает только за оформление,
 * содержимое передается слотами [header] и [content].
 *
 * @param isDismissible можно ли закрыть шит свайпом, тапом по фону или кнопкой назад.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    isDismissible: Boolean = true,
    header: (@Composable () -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val canDismiss by rememberUpdatedState(isDismissible)
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { value -> value != SheetValue.Hidden || canDismiss }
    )

    ModalBottomSheet(
        onDismissRequest = {
            if (canDismiss) onDismissRequest()
        },
        sheetState = sheetState,
        containerColor = KabanchikTheme.colors.background,
        dragHandle = { CommonBottomSheetDragHandle() },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            header?.invoke()
            content()
        }
    }
}

// Своя ручка вместо BottomSheetDefaults.DragHandle: у нее вертикальный отступ 22dp зашит внутри
@Composable
private fun CommonBottomSheetDragHandle() {
    Box(
        modifier = Modifier
            .padding(top = 22.dp, bottom = 16.dp)
            .size(width = 32.dp, height = 4.dp)
            .background(
                color = KabanchikTheme.colors.secondaryText,
                shape = CircleShape
            )
    )
}

/**
 * Стандартная шапка [CommonBottomSheet]: заголовок и кнопка закрытия.
 */
@Composable
fun CommonBottomSheetHeader(
    title: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCloseEnabled: Boolean = true,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = KabanchikTheme.typography.headlineSmall,
            color = KabanchikTheme.colors.mainText,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onCloseClick,
            enabled = isCloseEnabled
        ) {
            Icon(
                painter = KabanchikIcons.Close24,
                contentDescription = null,
                tint = KabanchikTheme.colors.secondaryText
            )
        }
    }
}
