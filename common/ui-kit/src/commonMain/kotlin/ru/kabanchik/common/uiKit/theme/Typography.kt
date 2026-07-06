package ru.kabanchik.common.uiKit.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object KabanchikTypography {
    val typography = Typography(
        headlineLarge = TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        ),
        headlineMedium = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        ),
        titleLarge = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal
        ),
        titleMedium = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        ),
        titleSmall = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        ),
        bodyMedium = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        ),
        bodySmall = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal
        ),
        labelSmall = TextStyle(
            fontSize = 10.sp,
            fontWeight = FontWeight.Light
        )
    )
}

val KabanchikTypography.h1: TextStyle
    get() = typography.headlineLarge

val KabanchikTypography.regularHeadline: TextStyle
    get() = typography.headlineMedium

val KabanchikTypography.bigTitle: TextStyle
    get() = typography.titleLarge

val KabanchikTypography.regularTitle: TextStyle
    get() = typography.titleMedium

val KabanchikTypography.smallTitle: TextStyle
    get() = typography.titleSmall

val KabanchikTypography.regularText: TextStyle
    get() = typography.bodyMedium

val KabanchikTypography.bodyS: TextStyle
    get() = typography.bodySmall

val KabanchikTypography.extraSmallText: TextStyle
    get() = typography.labelSmall