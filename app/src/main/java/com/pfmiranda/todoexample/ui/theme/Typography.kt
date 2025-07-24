package com.pfmiranda.todoexample.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight


val Typography = Typography(
    headlineSmall = TextStyle( // Headline6
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = TextPrimary
    ),
    bodyMedium = TextStyle( // Body2
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary
    ),
    bodySmall = TextStyle( // Caption
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary
    ),
)