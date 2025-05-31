package com.aZina0.circulationmaze

import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Global {
    var redrawAmount = 0
    var screenWidthDp: Int? = null
    var screenHeightDp: Int? = null

    fun print(text: String) {
        Log.d("CustomPrint", text)
    }

    fun relativeHeight(percent: Float): Dp {
        return (screenHeightDp!! * percent).dp
    }

    fun relativeWidth(percent: Float): Dp {
        return (screenWidthDp!! * percent).dp
    }

    fun relativeFont(percent: Float): TextUnit {
        return (screenHeightDp!! * percent).sp
    }
}