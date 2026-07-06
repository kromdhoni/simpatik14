package com.ypm14.simpatik.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ypm14.simpatik.R

// Modern Scholar Typography — refined hierarchy with proper line height & letter spacing
val ManropeFontFamily = FontFamily(
    Font(R.font.manrope_regular, weight = FontWeight.Normal),
    Font(R.font.manrope_medium, weight = FontWeight.Medium),
    Font(R.font.manrope_semibold, weight = FontWeight.SemiBold),
    Font(R.font.manrope_bold, weight = FontWeight.Bold),
)

val SimpatikTypography = Typography(
    displayLarge   = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold,      lineHeight = 36.sp, letterSpacing = (-0.5).sp, fontFamily = ManropeFontFamily),
    displayMedium  = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold,      lineHeight = 32.sp, letterSpacing = (-0.3).sp, fontFamily = ManropeFontFamily),
    displaySmall   = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold,  lineHeight = 28.sp, letterSpacing = 0.sp,     fontFamily = ManropeFontFamily),
    headlineLarge  = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold,      lineHeight = 32.sp, letterSpacing = (-0.3).sp, fontFamily = ManropeFontFamily),
    headlineMedium = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold,  lineHeight = 28.sp, letterSpacing = 0.sp,     fontFamily = ManropeFontFamily),
    headlineSmall  = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold,  lineHeight = 24.sp, letterSpacing = 0.sp,     fontFamily = ManropeFontFamily),
    titleLarge     = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold,  lineHeight = 24.sp, letterSpacing = 0.sp,     fontFamily = ManropeFontFamily),
    titleMedium    = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium,    lineHeight = 22.sp, letterSpacing = 0.15.sp,  fontFamily = ManropeFontFamily),
    titleSmall     = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium,    lineHeight = 20.sp, letterSpacing = 0.1.sp,   fontFamily = ManropeFontFamily),
    bodyLarge      = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal,    lineHeight = 24.sp, letterSpacing = 0.5.sp,   fontFamily = ManropeFontFamily),
    bodyMedium     = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal,    lineHeight = 20.sp, letterSpacing = 0.25.sp,  fontFamily = ManropeFontFamily),
    bodySmall      = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal,    lineHeight = 18.sp, letterSpacing = 0.4.sp,   fontFamily = ManropeFontFamily),
    labelLarge     = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.SemiBold,  lineHeight = 20.sp, letterSpacing = 0.1.sp,   fontFamily = ManropeFontFamily),
    labelMedium    = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium,    lineHeight = 16.sp, letterSpacing = 0.5.sp,   fontFamily = ManropeFontFamily),
    labelSmall     = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium,    lineHeight = 16.sp, letterSpacing = 0.5.sp,   fontFamily = ManropeFontFamily),
)
