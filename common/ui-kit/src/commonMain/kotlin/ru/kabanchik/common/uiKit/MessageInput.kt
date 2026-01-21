package ru.kabanchik.common.uiKit

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.regularText

@Composable
fun MessageInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = KabanchikTheme.typography.regularText,
        label = {
            if (label != null) {
                Text(text = label)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = KabanchikTheme.colors.MainText,
            unfocusedTextColor = KabanchikTheme.colors.MainText
        ),
        modifier = modifier
    )
}

@Composable
@Preview
private fun MessageInputEmptyPreview() {
    MessageInput(
        value = "",
        label = "Подсказка",
        onValueChange = {}
    )
}