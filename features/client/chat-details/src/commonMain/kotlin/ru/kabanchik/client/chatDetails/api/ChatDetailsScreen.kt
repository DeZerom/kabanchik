package ru.kabanchik.client.chatDetails.api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kabanchik.features.client.chat_details.generated.resources.Res
import kabanchik.features.client.chat_details.generated.resources.chat_details_hint
import org.jetbrains.compose.resources.stringResource
import ru.kabanchik.client.chatDetails.internal.MessageCard
import ru.kabanchik.common.uiKit.CircleButton
import ru.kabanchik.common.uiKit.CommonTextInput
import ru.kabanchik.common.uiKit.HSpacer
import ru.kabanchik.common.uiKit.KabanchikIcons
import ru.kabanchik.common.uiKit.VSpacer
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import ru.kabanchik.common.uiKit.theme.cardDefault

@Composable
fun ChatDetailsScreen() {
    var value by remember { mutableStateOf("") }

    Scaffold { paddingValues ->
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.padding(all = 16.dp)
            ) {
                MessageCard(
                    isUserAuthor = true,
                    messageDate = "24.12.2026",
                    messageTime = "19.45",
                    messageText = "Бубубу ратата три собаки два кота, хочу не знаю чего, не знаю когда. Сделай круто!"
                )
                VSpacer(16.dp)
                MessageCard(
                    isUserAuthor = false,
                    messageDate = "24.12.2026",
                    messageTime = "19.45",
                    messageText = "Бубубу ратата три собаки два кота, хочу не знаю чего, не знаю когда. Сделай круто!"
                )
                VSpacer(16.dp)
                Surface(
                    color = KabanchikTheme.colors.card,
                    shape = KabanchikTheme.shapes.cardDefault,
                ) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.padding(all = 16.dp)
                    ) {
                        CommonTextInput(
                            value = value,
                            onValueChange = { value = it },
                            label = stringResource(Res.string.chat_details_hint),
                            modifier = Modifier.weight(1f)
                        )
                        HSpacer(12.dp)
                        CircleButton(
                            onClick = {},
                            color = KabanchikTheme.colors.accent,
                            painter = KabanchikIcons.Send24,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }
}