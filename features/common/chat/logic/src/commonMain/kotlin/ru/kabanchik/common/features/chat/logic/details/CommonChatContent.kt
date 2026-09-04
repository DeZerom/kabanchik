package ru.kabanchik.common.features.chat.logic.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kabanchik.features.common.chat.logic.generated.resources.Res
import kabanchik.features.common.chat.logic.generated.resources.chat_details_operator_found
import kotlinx.io.Buffer
import ru.kabanchik.common.feature.chat.model.CommonPendingFile
import ru.kabanchik.common.feature.chat.model.CommonUiMessage
import ru.kabanchik.common.files.api.SelectedFile
import ru.kabanchik.common.modifier.keyboardInsetsPadding
import ru.kabanchik.common.modifier.sendMessageModifier
import ru.kabanchik.common.tools.textResource.TextResource
import ru.kabanchik.common.uiKit.icons.KabanchikIcons
import ru.kabanchik.common.uiKit.icons.extensions.Send24
import ru.kabanchik.common.uiKit.theme.KabanchikTheme

@Composable
fun CommonChatContent(
    messages: List<CommonUiMessage>,
    currentMessageText: String,
    selectedFiles: List<CommonPendingFile> = emptyList(),
    isSending: Boolean = false,
    onMessageTextChanged: (String) -> Unit,
    onMessageSent: () -> Unit,
    onFileSelectionRequested: () -> Unit = {},
    onFileRemoved: (CommonPendingFile) -> Unit = {},
    onImageClicked: (String) -> Unit = {},
    onFileClicked: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val initialScrollDone = remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(density)
    val previousImeBottom = remember { mutableStateOf(imeBottom) }
    val shouldScrollOnImeOpen = remember { mutableStateOf(false) }

    LaunchedEffect(imeBottom) {
        val isImeOpening = imeBottom > previousImeBottom.value

        if (isImeOpening && shouldScrollOnImeOpen.value && messages.isNotEmpty()) {
            listState.scrollToItem(messages.lastIndex)
        }

        if (imeBottom == 0) {
            shouldScrollOnImeOpen.value = false
        }

        previousImeBottom.value = imeBottom
    }

    LaunchedEffect(messages.size) {
        if (messages.isEmpty()) {
            initialScrollDone.value = false
            return@LaunchedEffect
        }

        val lastMessageIndex = messages.lastIndex

        if (!initialScrollDone.value) {
            listState.scrollToItem(lastMessageIndex)
            initialScrollDone.value = true
            return@LaunchedEffect
        }

        val layoutInfo = listState.layoutInfo
        val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
        val wasAtBottom = lastVisibleItem == null ||
                lastVisibleItem.index >= layoutInfo.totalItemsCount - 2

        if (wasAtBottom) {
            listState.animateScrollToItem(lastMessageIndex)
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .keyboardInsetsPadding()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = if (selectedFiles.isEmpty()) 88.dp else 168.dp,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(
                    items = messages,
                    key = { it.id }
                ) { message ->
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CommonChatUiMessage(
                            message = message,
                            onImageClicked = onImageClicked,
                            onFileClicked = onFileClicked,
                            modifier = Modifier
                                .align(message.horizontalAlignment)
                                .then(
                                    if (message is CommonUiMessage.Message) {
                                        Modifier.fillMaxWidth(0.8f)
                                    } else {
                                        Modifier
                                    }
                                )
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                IconButton(
                    onClick = onFileSelectionRequested,
                    enabled = !isSending,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color = KabanchikTheme.colors.background,
                                    shape = CircleShape
                                )
                        )
                        Icon(
                            painter = KabanchikIcons.Plus24,
                            contentDescription = null,
                            tint = KabanchikTheme.colors.accent
                        )
                    }
                }
                CommonChatMessageInput(
                    value = currentMessageText,
                    onValueChange = onMessageTextChanged,
                    selectedFiles = selectedFiles,
                    onFileRemoved = onFileRemoved,
                    modifier = Modifier
                        .weight(1f)
                        .sendMessageModifier(onMessageSent)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused && imeBottom == 0) {
                                shouldScrollOnImeOpen.value =
                                    listState.isItemVisible(messages.lastIndex)
                            }
                        }
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(48.dp)
                ) {
                    if (isSending) {
                        CircularProgressIndicator(
                            color = KabanchikTheme.colors.accent,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp),
                        )
                    } else {
                        IconButton(onClick = onMessageSent) {
                            Box(contentAlignment = Alignment.Center) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            color = KabanchikTheme.colors.background,
                                            shape = CircleShape
                                        )
                                )
                                Icon(
                                    imageVector = KabanchikIcons.Send24,
                                    contentDescription = null,
                                    tint = KabanchikTheme.colors.accent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun LazyListState.isItemVisible(index: Int): Boolean {
    return index >= 0 && layoutInfo.visibleItemsInfo.any { it.index == index }
}

private val CommonUiMessage.horizontalAlignment: Alignment
    get() = when (this) {
        is CommonUiMessage.Date, is CommonUiMessage.SystemMessage -> Alignment.Center
        is CommonUiMessage.Message -> {
            if (isUserAuthor) Alignment.CenterEnd else Alignment.CenterStart
        }
    }

@Preview
@Composable
private fun CommonChatContentEmptyPreview() {
    KabanchikTheme {
        Scaffold {
            CommonChatContent(
                messages = emptyList(),
                currentMessageText = "",
                onMessageTextChanged = {},
                onMessageSent = {}
            )
        }
    }
}

@Preview
@Composable
private fun CommonChatContentWithMessagesPreview() {
    KabanchikTheme {
        Scaffold {
            CommonChatContent(
                messages = ChatDetailsMock.messages,
                currentMessageText = "Спасибо, подойдет",
                onMessageTextChanged = {},
                onMessageSent = {}
            )
        }
    }
}

@Preview
@Composable
private fun CommonChatContentDifferentDatesPreview() {
    KabanchikTheme {
        Scaffold {
            CommonChatContent(
                messages = ChatDetailsMock.messagesFromDifferentDates,
                currentMessageText = "",
                onMessageTextChanged = {},
                onMessageSent = {}
            )
        }
    }
}

@Preview
@Composable
private fun CommonChatContentWithSelectedFilesPreview() {
    KabanchikTheme {
        Scaffold {
            CommonChatContent(
                messages = ChatDetailsMock.messages,
                currentMessageText = "Сообщение с файлами",
                selectedFiles = ChatDetailsMock.selectedFiles,
                onMessageTextChanged = {},
                onMessageSent = {},
            )
        }
    }
}

@Preview
@Composable
private fun CommonChatContentSendingFilesPreview() {
    KabanchikTheme {
        Scaffold {
            CommonChatContent(
                messages = ChatDetailsMock.messages,
                currentMessageText = "Отправляю документы",
                selectedFiles = ChatDetailsMock.selectedFiles,
                isSending = true,
                onMessageTextChanged = {},
                onMessageSent = {},
            )
        }
    }
}

private object ChatDetailsMock {
    private val operatorFound = CommonUiMessage.SystemMessage(
        id = "operator-found",
        message = TextResource.Id(Res.string.chat_details_operator_found)
    )

    val messages = listOf(
        CommonUiMessage.Date(
            id = "date-today",
            date = "7 июля"
        ),
        operatorFound,
        CommonUiMessage.Message(
            id = "message-1",
            authorLogin = "operator",
            isUserAuthor = false,
            time = "14:08",
            text = "Здравствуйте! Я подключился к чату и помогу с заказом."
        ),
        CommonUiMessage.Message(
            id = "message-2",
            authorLogin = "client",
            isUserAuthor = true,
            time = "14:10",
            text = "Добрый день. Нужно уточнить детали по бронированию ресторана."
        ),
        CommonUiMessage.Message(
            id = "message-3",
            authorLogin = "operator",
            isUserAuthor = false,
            time = "14:12",
            text = "Конечно. На какую дату и сколько гостей планируете?"
        )
    )

    val selectedFiles = listOf(
        CommonPendingFile(
            id = "preview-pdf",
            file = SelectedFile(
                fileName = "Договор.pdf",
                contentType = "application/pdf",
                size = 3_270_246,
                previewUri = "file:///preview.pdf",
                sourceProvider = { Buffer() },
            )
        ),
        CommonPendingFile(
            id = "preview-docx",
            file = SelectedFile(
                fileName = "Условия.docx",
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                size = 92_160,
                previewUri = "file:///preview.docx",
                sourceProvider = { Buffer() },
            )
        ),
        CommonPendingFile(
            id = "preview-image",
            file = SelectedFile(
                fileName = "Фото.jpg",
                contentType = "image/jpeg",
                size = 512_000,
                previewUri = "file:///preview.jpg",
                sourceProvider = { Buffer() },
            )
        ),
    )

    val messagesFromDifferentDates = listOf(
        CommonUiMessage.Date(
            id = "date-july-5",
            date = "5 июля"
        ),
        CommonUiMessage.Message(
            id = "message-4",
            authorLogin = "client",
            isUserAuthor = true,
            time = "18:42",
            text = "Хочу забронировать стол на выходные."
        ),
        CommonUiMessage.Message(
            id = "message-5",
            authorLogin = "operator",
            isUserAuthor = false,
            time = "18:44",
            text = "Подскажите, пожалуйста, город и желаемое время."
        ),
        CommonUiMessage.Date(
            id = "date-july-6",
            date = "6 июля"
        ),
        operatorFound.copy(id = "operator-found-second-day"),
        CommonUiMessage.Message(
            id = "message-6",
            authorLogin = "operator",
            isUserAuthor = false,
            time = "09:15",
            text = "Нашел несколько вариантов рядом с центром."
        ),
        CommonUiMessage.Message(
            id = "message-7",
            authorLogin = "client",
            isUserAuthor = true,
            time = "09:17",
            text = "Отлично, пришлите вариант с тихим залом."
        ),
        CommonUiMessage.Date(
            id = "date-july-7",
            date = "7 июля"
        ),
        CommonUiMessage.Message(
            id = "message-8",
            authorLogin = "operator",
            isUserAuthor = false,
            time = "11:03",
            text = "Бронь подтверждена на 17:00, гостей будут ждать у входа."
        )
    )
}
