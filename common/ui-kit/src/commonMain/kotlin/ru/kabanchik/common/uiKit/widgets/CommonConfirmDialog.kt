package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyMedium
import ru.kabanchik.common.uiKit.theme.cardDefault
import ru.kabanchik.common.uiKit.theme.headlineSmall

@Composable
fun CommonConfirmDialog(
    title: String,
    text: String?,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isConfirmLoading: Boolean = false,
) {
    Dialog(
        onDismissRequest = {
            if (!isConfirmLoading) {
                onDismiss()
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = !isConfirmLoading,
            dismissOnClickOutside = !isConfirmLoading
        )
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = DialogMaxWidth)
                .fillMaxWidth()
                .clip(KabanchikTheme.shapes.cardDefault)
                .background(KabanchikTheme.colors.card)
                .padding(all = 24.dp)
        ) {
            Text(
                text = title,
                style = KabanchikTheme.typography.headlineSmall,
                color = KabanchikTheme.colors.mainText
            )
            if (text != null) {
                VSpacer(16.dp)
                Text(
                    text = text,
                    style = KabanchikTheme.typography.bodyMedium,
                    color = KabanchikTheme.colors.secondaryText
                )
            }
            VSpacer(24.dp)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CommonButton(
                    text = dismissText,
                    onClick = onDismiss,
                    backgroundColor = KabanchikTheme.colors.background,
                    textColor = KabanchikTheme.colors.mainText,
                    isEnabled = !isConfirmLoading,
                    modifier = Modifier.weight(1f)
                )
                CommonButton(
                    text = confirmText,
                    onClick = onConfirm,
                    backgroundColor = KabanchikTheme.colors.accent,
                    textColor = KabanchikTheme.colors.mainText,
                    isLoading = isConfirmLoading,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private val DialogMaxWidth = 400.dp

@Preview
@Composable
private fun CommonConfirmDialogPreview() {
    KabanchikTheme {
        CommonConfirmDialog(
            title = "Выйти из аккаунта?",
            text = "Вам потребуется снова войти в аккаунт, чтобы пользоваться приложением",
            confirmText = "Выйти",
            dismissText = "Отмена",
            onConfirm = {},
            onDismiss = {}
        )
    }
}
