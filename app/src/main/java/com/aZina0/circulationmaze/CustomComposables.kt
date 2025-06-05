package com.aZina0.circulationmaze

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
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
fun CustomHeader(
    displayProgressBar: Boolean,
    firstComposable: @Composable (() -> Unit)? = null,
    middleComposable: @Composable (() -> Unit)? = null,
    lastComposable: @Composable (() -> Unit)? = null,
) {
    Column (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(Global.relativeHeight(0.075f)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                firstComposable?.invoke()
            }
            middleComposable?.invoke()
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                lastComposable?.invoke()
            }
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