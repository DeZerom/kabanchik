package ru.kabanchik.common.features.chat.logic.details

import kotlin.test.Test
import kotlin.test.assertEquals

class CommonChatAttachmentsTest {
    @Test
    fun createsExpectedRowsForImageCounts() {
        assertEquals(emptyList(), emptyList<Int>().toImageRows())
        assertEquals(listOf(listOf(1)), (1..1).toList().toImageRows())
        assertEquals(listOf(listOf(1, 2)), (1..2).toList().toImageRows())
        assertEquals(listOf(listOf(1), listOf(2, 3)), (1..3).toList().toImageRows())
        assertEquals(listOf(listOf(1, 2), listOf(3, 4)), (1..4).toList().toImageRows())
        assertEquals(
            listOf(listOf(1), listOf(2, 3), listOf(4, 5)),
            (1..5).toList().toImageRows()
        )
    }
}
