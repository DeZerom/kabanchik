package ru.kabanchik.common.uiKit.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.dp
import kabanchik.common.ui_kit.generated.resources.Res
import kabanchik.common.ui_kit.generated.resources.chat_details_you
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.bodyMedium
import ru.kabanchik.common.uiKit.theme.cardDefault
import ru.kabanchik.common.uiKit.theme.extraSmallText

@Composable
fun CommonMessageCard(
    isUserAuthor: Boolean,
    messageTime: String,
    messageText: String,
    authorLogin: String,
    modifier: Modifier = Modifier,
    attachmentsContent: (@Composable ColumnScope.() -> Unit)? = null,
) {
    Surface(
        color = if (isUserAuthor) KabanchikTheme.colors.accent else KabanchikTheme.colors.card,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = if (isUserAuthor) {
                        stringResource(Res.string.chat_details_you)
                    } else {
                        authorLogin
                    },
                    style = KabanchikTheme.typography.extraSmallText,
                    color = KabanchikTheme.colors.secondaryText,
                )
                Text(
                    text = messageTime,
                    style = KabanchikTheme.typography.extraSmallText,
                    color = KabanchikTheme.colors.secondaryText,
                )
            }
            attachmentsContent?.let { content ->
                VSpacer(8.dp)
                content()
            }
            if (messageText.isNotBlank()) {
                VSpacer(8.dp)
                Text(
                    text = messageText,
                    style = KabanchikTheme.typography.bodyMedium,
                    color = KabanchikTheme.colors.mainText,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            VSpacer(16.dp)
        }
    }
}
