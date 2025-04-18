package com.example.circulationmaze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.circulationmaze.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        window.setNavigationBarContrastEnforced(false)
        setContent {
            AppTheme (
                dynamicColor = false,
            ) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RegisterScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
//        Game.createNewGame(this, 3095248787, 13, 13)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasQuadrantSize = size / 2F
        drawRect(
            color = Color.Magenta,
            size = canvasQuadrantSize
        )
    }
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
    PieceElement(modifier = modifier)
}


@Composable
fun PieceElement(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    )
    Image(
        painter = painterResource(id = R.drawable.i_piece),
        contentDescription = null
    )
}
