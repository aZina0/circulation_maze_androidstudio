package com.aZina0.circulationmaze

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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

@Composable
fun CustomHeader(title: String, displayProgressBar: Boolean) {
    Column (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .size(Global.relativeHeight(0.075f)),
            contentAlignment = Alignment.Center,
        ) {
            Text (
                text = title,
                fontSize = Global.relativeFont(0.04f),
            )

        }
        if (displayProgressBar) {
            LinearProgressIndicator(
                modifier = Modifier
                    .width(Global.relativeWidth(.7f))
                    .height(Global.relativeHeight(0.005f)
                )
            )
        } else {
            Box (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Global.relativeHeight(0.005f)
                )
            )
        }
    }

}