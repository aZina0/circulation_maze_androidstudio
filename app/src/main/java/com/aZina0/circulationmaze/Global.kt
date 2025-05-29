package com.aZina0.circulationmaze

import android.util.Log

object Global {
    var redrawAmount = 0
    var screenWidthDp: Int? = null

    fun print(text: String) {
        Log.d("CustomPrint", text)
    }
}