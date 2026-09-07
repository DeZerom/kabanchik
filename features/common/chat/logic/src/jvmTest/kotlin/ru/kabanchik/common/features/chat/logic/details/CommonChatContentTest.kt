package ru.kabanchik.common.features.chat.logic.details

import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.runComposeUiTest
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.uiKit.theme.KabanchikTheme
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class CommonChatContentTest {
    @Test
    fun restoresScrollPositionAfterReturningToChat(): Unit = runComposeUiTest {
        val messages = List(MessageCount) { index ->
            CommonUiMessage.Message(
                id = "message-$index",
                text = "Message $index",
            )
        }
        var screen by mutableStateOf(Screen.Chat)

        setContent {
            val stateHolder = rememberSaveableStateHolder()

            when (screen) {
                Screen.Chat -> {
                    stateHolder.SaveableStateProvider(ChatStateKey) {
                        KabanchikTheme {
                            CommonChatContent(
                                messages = messages,
                                currentMessageText = "",
                                onMessageTextChanged = {},
                                onMessageSent = {},
                            )
                        }
                    }
                }

                Screen.ImageViewer -> Text(ImageViewerText)
            }
        }

        waitForIdle()
        onNodeWithTag(CHAT_MESSAGES_TEST_TAG).performScrollToIndex(MiddleMessageIndex)
        waitForIdle()

        val positionBefore = middleMessagePosition()

        runOnIdle {
            screen = Screen.ImageViewer
        }
        onNodeWithText(ImageViewerText).assertIsDisplayed()

        runOnIdle {
            screen = Screen.Chat
        }
        waitForIdle()

        assertEquals(
            expected = positionBefore,
            actual = middleMessagePosition(),
            absoluteTolerance = PositionTolerance,
        )
    }

    private fun androidx.compose.ui.test.ComposeUiTest.middleMessagePosition(): Float {
        return onNodeWithText("Message $MiddleMessageIndex")
            .assertIsDisplayed()
            .fetchSemanticsNode()
            .boundsInRoot
            .top
    }

    private enum class Screen {
        Chat,
        ImageViewer,
    }

    private companion object {
        const val MessageCount = 40
        const val MiddleMessageIndex = 15
        const val ChatStateKey = "chat"
        const val ImageViewerText = "Image viewer"
        const val PositionTolerance = 1f
    }
}
