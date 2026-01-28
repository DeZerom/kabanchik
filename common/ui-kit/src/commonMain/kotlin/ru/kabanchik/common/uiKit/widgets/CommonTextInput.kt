package ru.kabanchik.common.uiKit.widgets

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.regularText

@Composable
fun CommonTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
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
            focusedTextColor = KabanchikTheme.colors.mainText,
            unfocusedTextColor = KabanchikTheme.colors.mainText
        ),
        visualTransformation = visualTransformation,
        modifier = modifier
    )
}

@Composable
@Preview
private fun CommonTextInputEmptyPreview() {
    CommonTextInput(
        value = "",
        label = "Подсказка",
        onValueChange = {}
    )
}