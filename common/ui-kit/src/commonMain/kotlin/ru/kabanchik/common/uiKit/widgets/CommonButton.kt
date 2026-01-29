package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.cardDefault
import ru.kabanchik.common.uiKit.theme.regularText

@Composable
fun CommonButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ButtonDefaults.buttonColors().containerColor,
    textColor: Color = ButtonDefaults.buttonColors().contentColor,
    isEnabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = {
            if (!isLoading) {
                onClick()
            }
        },
        shape = KabanchikTheme.shapes.cardDefault,
        content = {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = textColor
                )
            } else {
                Text(
                    text = text,
                    style = KabanchikTheme.typography.regularText,
                    color = textColor
                )
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
        ),
        enabled = isEnabled,
        modifier = modifier.heightIn(min = 56.dp)
    )
}