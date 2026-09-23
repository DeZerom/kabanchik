package ru.kabanchik.pro.data.chatDetails.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProApiShift(
    @SerialName("status")
    val status: ProApiShiftStatus,
)

@Serializable
enum class ProApiShiftStatus {
    @SerialName("OFF_SHIFT")
    OffShift,

    @SerialName("ON_SHIFT")
    OnShift,
}
