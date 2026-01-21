package ru.kabanchik.client.chatDetails.api

import androidx.compose.foundation.layout.Box
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
import ru.kabanchik.common.uiKit.CommonTextInput
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
            Surface(
                color = KabanchikTheme.colors.card,
                shape = KabanchikTheme.shapes.cardDefault,
                modifier = Modifier.padding(all = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(all = 16.dp)
                ) {
                    CommonTextInput(
                        value = value,
                        onValueChange = { value = it },
                        label = "Hint",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}