package com.aZina0.circulationmaze

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.aZina0.circulationmaze.game.GameScreen
import com.aZina0.circulationmaze.mainMenu.MainMenuScreen
import com.aZina0.circulationmaze.newGame.NewGameScreen
import com.aZina0.circulationmaze.registerLogin.LoginScreen
import com.aZina0.circulationmaze.registerLogin.RegisterScreen
import com.aZina0.circulationmaze.ui.theme.AppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Global.print("signInAnonymously:success")
                } else {
                    // If sign in fails, display a message to the user.
                    Global.print("signInAnonymously:failure")
                    Global.print(task.exception.toString())
                }
            }

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )

        setContent { CirculationMazeApp() }
    }
}


@Serializable
object MainMenu

@Serializable
object NewGame

@Serializable
object Register

@Serializable
object Login

@Serializable
object Settings

@Serializable
object Profile

@Serializable
data class Game(val gridSize: Int, val seed: Long)

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

                NavHost(navController = navController, startDestination = MainMenu) {
                    composable<MainMenu> {
                        MainMenuScreen(
                            onContinueClick = {},
                            onNewGameClick = {
                                navController.navigate(route = NewGame)
                            },
                            onLoginClick = {
                                navController.navigate(route = Login)
                            }
                        )
                    }

                    composable<NewGame> {
                        NewGameScreen(
                            onStartClicked = { gridSize, seed ->
                                navController.navigate(route = Game(gridSize, seed))
                            },
                        )
                    }

                    composable<Game> { backStackEntry ->
                        val game: Game = backStackEntry.toRoute()
                        GameScreen(
                            gridSize = game.gridSize,
                            seed = game.seed,
                            onReturnClicked = {
                                navController.navigate(route = MainMenu)
                            }
                        )
                    }

                    composable<Login> {
                        LoginScreen(
                            onSwapToRegisterClick = {
                                navController.navigate(route = Register)
                            },
                        )
                    }

                    composable<Register> {
                        RegisterScreen (
                            Modifier,
                            onSwapToLoginClick = {
                                navController.navigate(route = Login)
                            },
                        )
                    }

                }

                BackHandler(enabled = true) {

                }
            }
        }
    }
}