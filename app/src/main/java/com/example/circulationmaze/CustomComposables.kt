package com.example.circulationmaze

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun RelativeVerticalSpacer(percent: Float) {
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    Spacer(
        modifier = Modifier.height(screenHeightDp * percent)
    )
}