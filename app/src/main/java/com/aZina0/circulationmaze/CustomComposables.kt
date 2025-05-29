package com.aZina0.circulationmaze

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RelativeVerticalSpacer(percent: Float) {
    Spacer(
        modifier = Modifier.height(Global.relativeHeight(percent))
    )
}

@Composable
fun RelativeHorizontalSpacer(percent: Float) {
    Spacer(
        modifier = Modifier.width(Global.relativeWidth(percent))
    )
}