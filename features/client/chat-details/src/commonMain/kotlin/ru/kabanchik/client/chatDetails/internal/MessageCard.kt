package ru.kabanchik.client.chatDetails.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kabanchik.features.client.chat_details.generated.resources.Res
import kabanchik.features.client.chat_details.generated.resources.chat_details_date_time
import kabanchik.features.client.chat_details.generated.resources.chat_details_operator
import kabanchik.features.client.chat_details.generated.resources.chat_details_you
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.cardDefault
import ru.kabanchik.common.uiKit.theme.extraSmallText
import ru.kabanchik.common.uiKit.theme.regularText

@Composable
internal fun MessageCard(
    isUserAuthor: Boolean,
    messageDate: String,
    messageTime: String,
    messageText: String,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isUserAuthor) KabanchikTheme.colors.accent else KabanchikTheme.colors.card,
        shape = KabanchikTheme.shapes.cardDefault,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, bottom = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(
                        if (isUserAuthor) Res.string.chat_details_you else Res.string.chat_details_operator
                    ),
                    style = KabanchikTheme.typography.extraSmallText,
                    color = KabanchikTheme.colors.secondaryText,
                )
                Text(
                    text = stringResource(Res.string.chat_details_date_time, messageDate, messageTime),
                    style = KabanchikTheme.typography.extraSmallText,
                    color = KabanchikTheme.colors.secondaryText,
                )
            }
            VSpacer(8.dp)
            Text(
                text = messageText,
                style = KabanchikTheme.typography.regularText,
                color = KabanchikTheme.colors.mainText
            )
        }
    }
}