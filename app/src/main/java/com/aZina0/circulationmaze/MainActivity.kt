package com.aZina0.circulationmaze

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aZina0.circulationmaze.boardDetails.BoardDetailsScreen
import com.aZina0.circulationmaze.editProfile.EditProfileScreen
import com.aZina0.circulationmaze.game.GameScreen
import com.aZina0.circulationmaze.leaderboards.LeaderboardsScreen
import com.aZina0.circulationmaze.loadGame.LoadGameScreen
import com.aZina0.circulationmaze.mainMenu.MainMenuScreen
import com.aZina0.circulationmaze.newGame.NewGameScreen
import com.aZina0.circulationmaze.profile.ProfileScreen
import com.aZina0.circulationmaze.registerLogin.LoginScreen
import com.aZina0.circulationmaze.registerLogin.RegisterScreen
import com.aZina0.circulationmaze.ui.theme.AppTheme
import com.aZina0.circulationmaze.userList.UserListScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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
open class Route

@Serializable
object MainMenuRoute: Route()

@Serializable
object LoadGameRoute

@Serializable
object NewGameRoute

@Serializable
data class LeaderboardsRoute(
    val tabIndex: Int,
)

@Serializable
object SettingsRoute

@Serializable
object RegisterRoute

@Serializable
object LoginRoute

@Serializable
data class ProfileRoute(
    val userUid: String,
    val highlightAllSolvedBoards: Boolean = false,
)

@Serializable
data class EditProfileRoute(
    val userUid: String,
)

@Serializable
data class GameRoute(
    val startType: String,
    val uid: String,
    val seed: Long = 0L,
    val gridSize: Int = 0,
)

@Serializable
data class BoardDetailsRoute(
    val boardUid: String,
)

@Serializable
data class UserListRoute(
    val type: String,
    val userUid: String = "",
)

@Composable
fun CirculationMazeApp() {
    AppTheme (
        darkTheme = true,
        dynamicColor = false,
    ) {

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->

            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "picture",
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
//                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
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
                                navController.navigate(
                                    route =
                                    LeaderboardsRoute(
                                        tabIndex = 0
                                    )
                                )
                            },
                            onSettingsClick = {
                                navController.navigate(route = SettingsRoute)
                            },
                            onLoginClick = {
                                navController.navigate(route = LoginRoute)
                            },
                            onRegisterClick = {
                                navController.navigate(route = RegisterRoute)
                            },
                            onProfileClicked = {
                                navController.navigate(
                                    route = ProfileRoute(
                                        userUid = Firebase.auth.currentUser?.uid ?: ""
                                    )
                                )
                            },
                        )
                    }

                    composable<LoadGameRoute> {
                        LoadGameScreen(
                            onSaveClicked = { uid ->
                                navController.navigate(
                                    route = GameRoute(
                                        startType = "loadGame",
                                        uid = uid,
                                    )
                                )
                            },
                            onViewSolvedBoardsClicked = {
                                navController.navigate(route = ProfileRoute(
                                    userUid = Firebase.auth.currentUser?.uid ?: "",
                                    highlightAllSolvedBoards = true,
                                ))
                            },
                            onReturnClicked = {
                                navController.navigate(route = MainMenuRoute)
                            },
                        )
                    }

                    composable<NewGameRoute> {
                        NewGameScreen(
                            onStartClicked = { uid, seed, gridSize ->
                                navController.navigate(
                                    route = GameRoute(
                                        startType = "newGame",
                                        uid = uid,
                                        seed = seed,
                                        gridSize = gridSize,
                                    )
                                )
                            },
                            onReturnClicked = {
                                navController.navigate(route = MainMenuRoute)
                            },
                        )
                    }

                    composable<GameRoute> {
                        GameScreen(
                            onReturnClicked = {
                                navController.navigate(route = MainMenuRoute)
                            },
                            onStartNewGameClicked = {
                                navController.navigate(route = NewGameRoute)
                            },
                        )
                    }

                    composable<LeaderboardsRoute> {
                        LeaderboardsScreen(
                            onBoardClicked = { boardUid ->
                                navController.navigate(
                                    route = BoardDetailsRoute(
                                        boardUid = boardUid,
                                    )
                                )
                            },
                            onReturnClicked = {
                                navController.navigate(route = MainMenuRoute)
                            },
                        )
                    }

                    composable<BoardDetailsRoute> {
                        BoardDetailsScreen(
                            onReturnClicked = {
                                navController.popBackStack()
                            },
                            onProfileClicked = { userUid ->
                                navController.navigate(route = ProfileRoute(
                                    userUid = userUid,
                                ))
                            },
                        )
                    }

                    composable<LoginRoute> {
                        LoginScreen(
                            onSwapToRegisterClick = {
                                navController.navigate(route = RegisterRoute)
                            },
                            onSuccessfulLogin = {
                                navController.navigate(route = MainMenuRoute)
                            },
                            onReturnClicked = {
                                navController.navigate(route = MainMenuRoute)
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
                            onReturnClicked =  {
                                navController.navigate(route = MainMenuRoute)
                            },
                        )
                    }

                    composable<ProfileRoute> {
                        ProfileScreen(
                            onEditProfileClicked = {
                                navController.navigate(
                                    route = EditProfileRoute(
                                        userUid = it
                                    )
                                )
                            },
                            onSignOut = {
                                navController.navigate(route = MainMenuRoute)
                            },
                            onBoardClicked = { boardUid ->
                                navController.navigate(
                                    route = BoardDetailsRoute(
                                        boardUid = boardUid,
                                    )
                                )
                            },
                            onReturnClicked = {
                                val route = Global.getRouteName(navController)
                                if (route == "EditProfileRoute") {
                                    navController.navigate(route = MainMenuRoute)
                                } else {
                                    navController.popBackStack()
                                }
                            },
                            onFollowersClicked = { userUid ->
                                navController.navigate(
                                    route = UserListRoute(
                                        type = "followers",
                                        userUid = userUid
                                    )
                                )
                            },
                            onFollowingClicked = { userUid ->
                                navController.navigate(
                                    route = UserListRoute(
                                        type = "following",
                                        userUid = userUid
                                    )
                                )
                            },
                        )
                    }

                    composable<EditProfileRoute> {
                        EditProfileScreen(
                            onReturnClicked = {
                                navController.navigate(
                                    route = ProfileRoute(it)
                                )
                            },
                        )
                    }

                    composable<UserListRoute> {
                        UserListScreen(
                            onReturnClicked = {
                                navController.popBackStack()
                            },
                            onUserClicked = { userUid ->
                                navController.navigate(
                                    route = ProfileRoute(userUid)
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}