package com.aZina0.circulationmaze

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aZina0.circulationmaze.game.GameScreen
import com.aZina0.circulationmaze.loadGame.LoadGameScreen
import com.aZina0.circulationmaze.mainMenu.MainMenuScreen
import com.aZina0.circulationmaze.newGame.NewGameScreen
import com.aZina0.circulationmaze.registerLogin.LoginScreen
import com.aZina0.circulationmaze.registerLogin.RegisterScreen
import com.aZina0.circulationmaze.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        setContent { CirculationMazeApp() }
    }
}


@Serializable
object MainMenuRoute

@Serializable
object LoadGameRoute

@Serializable
object NewGameRoute

@Serializable
object LeaderboardsRoute

@Serializable
object SettingsRoute

@Serializable
object RegisterRoute

@Serializable
object LoginRoute

@Serializable
object ProfileRoute

@Serializable
data class GameRoute(
    val startType: String,
    val uid: String,
    val seed: Long = 0L,
    val gridSize: Int = 0,
)

@Composable
fun CirculationMazeApp() {
    AppTheme (
        dynamicColor = false,
    ) {

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Global.screenWidthDp = LocalConfiguration.current.screenWidthDp
                Global.screenHeightDp = LocalConfiguration.current.screenHeightDp
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = MainMenuRoute,
                    enterTransition = {
                        fadeIn(animationSpec = tween(durationMillis = 250))
                    },
                    exitTransition = {
                        fadeOut(animationSpec = tween(durationMillis = 250))
                    }
                ) {
                    composable<MainMenuRoute> {
                        MainMenuScreen(
                            onContinueClick = {
                                navController.navigate(route = LoadGameRoute)
                            },
                            onNewGameClick = {
                                navController.navigate(route = NewGameRoute)
                            },
                            onLeaderboardsClick = {
                                navController.navigate(route = LeaderboardsRoute)
                            },
                            onSettingsClick = {
                                navController.navigate(route = SettingsRoute)
                            },
                            onLoginClick = {
                                navController.navigate(route = LoginRoute)
                            },
                        )
                    }

                    composable<LoadGameRoute> {
                        LoadGameScreen(
                            onSaveClicked = { uid ->
                                navController.navigate(route = GameRoute(
                                    startType = "loadGame",
                                    uid = uid,
                                ))
                            }
                        )
                    }

                    composable<NewGameRoute> {
                        NewGameScreen(
                            onStartClicked = { uid, seed, gridSize ->
                                navController.navigate(route = GameRoute(
                                    startType = "newGame",
                                    uid = uid,
                                    seed = seed,
                                    gridSize = gridSize,
                                ))
                            },
                        )
                    }

                    composable<GameRoute> {
                        GameScreen(
                            onReturnClicked = {
                                navController.navigate(route = MainMenuRoute)
                            }
                        )
                    }

                    composable<LoginRoute> {
                        LoginScreen(
                            onSwapToRegisterClick = {
                                navController.navigate(route = RegisterRoute)
                            },
                        )
                    }

                    composable<RegisterRoute> {
                        RegisterScreen(
                            onSwapToLoginClick = {
                                navController.navigate(route = LoginRoute)
                            },
                            onSuccessfulRegister = {
                                navController.navigate(route = MainMenuRoute)
                            },
                        )
                    }

                }
            }
        }
    }
}