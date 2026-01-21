package ru.kabanchik.client.chatDetails.api

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.kabanchik.common.uiKit.MessageInput

@Composable
fun ChatDetailsScreen() {
    var value by remember { mutableStateOf("") }

    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            MessageInput(
                value = value,
                label = "Hint",
                onValueChange = { value = it }
            )
        }
    }
}