package com.aZina0.circulationmaze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.aZina0.circulationmaze.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )

        setContent { CirculationMazeApp() }
    }
}



@Composable
fun CirculationMazeApp() {
    AppTheme (
        dynamicColor = false,
    ) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),

        ) { innerPadding ->
            Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                val navController = rememberNavController()
            }
        }
    }
}