package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.icons.extensions.EyeOff16
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyMedium

@Composable
fun CommonTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    trailingIcon: (@Composable () -> Unit)? = null,
    shape: Shape = RoundedCornerShape(12.dp),
    placeholder: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = KabanchikTheme.typography.bodyMedium,
        shape = shape,
        label = {
            if (label != null) {
                Text(text = label)
            }
        },
        placeholder = {
            if (placeholder != null) {
                Text(text = placeholder)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = KabanchikTheme.colors.mainText,
            unfocusedTextColor = KabanchikTheme.colors.mainText,
            focusedContainerColor = KabanchikTheme.colors.background,
            unfocusedContainerColor = KabanchikTheme.colors.background
        ),
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        modifier = modifier
    )
}

@Composable
@Preview
private fun CommonTextInputEmptyPreview() {
    KabanchikTheme {
        CommonTextInput(
            value = "",
            label = "Подсказка",
            onValueChange = {}
        )
    }
}

@Composable
@Preview
private fun CommonTextInputWithTextPreview() {
    KabanchikTheme {
        CommonTextInput(
            value = "Qwe",
            label = "Подсказка",
            onValueChange = {}
        )
    }
}

@Composable
@Preview
private fun CommonTextInputWithTrailingPreview() {
    KabanchikTheme {
        CommonTextInput(
            value = "Qwe",
            label = "Подсказка",
            onValueChange = {},
            trailingIcon = {
                Icon(
                    imageVector = KabanchikIcons.EyeOff16,
                    contentDescription = null
                )
            }
        )
    }
}