package mock

import kotlinx.datetime.LocalDateTime
import ru.kabanchik.common.chat.model.CommonMessage
import ru.kabanchik.common.chat.model.CommonMessageType

object MockData {
    object Messages {
        val message1 = CommonMessage(
            id = "q",
            authorLogin = "qwe",
            text = "qwewqe",
            type = CommonMessageType.Text,
            time = LocalDateTime(2020, 1, 1, 1, 1, 1)
        )

        val message2 = CommonMessage(
            id = "w",
            authorLogin = "qwe",
            text = "qwewqe",
            type = CommonMessageType.Text,
            time = LocalDateTime(2020, 1, 1, 2, 1, 1)
        )

        val message3 = CommonMessage(
            id = "e",
            authorLogin = "qwe",
            text = "qwewqe",
            type = CommonMessageType.Text,
            time = LocalDateTime(2020, 1, 2, 1, 1, 1)
        )

        val oneDayMessages = listOf(message1, message2)
        val allMessages = listOf(message1, message2, message3)
    }
}