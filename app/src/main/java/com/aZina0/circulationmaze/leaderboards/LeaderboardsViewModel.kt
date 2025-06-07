package com.aZina0.circulationmaze.leaderboards

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.AccountManager
import com.aZina0.circulationmaze.BoardInfo
import com.aZina0.circulationmaze.BoardManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class LeaderboardsInfo(
    val boardInfo: BoardInfo,
    val username: String,
)

@HiltViewModel
class LeaderboardsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val boardManager: BoardManager,
    private val accountManager: AccountManager,
): ViewModel() {
    val tabs = (5..51).filter { it%2 != 0 }

    var loadingBarActive by mutableStateOf(false)
        private set

    var selectedTabIndex by mutableStateOf(0)
        private set

    var bestBoardsInfo by mutableStateOf(listOf<LeaderboardsInfo>())

    init {
        selectedTabIndex = savedStateHandle["tabIndex"] ?: 0
        onTabClicked(selectedTabIndex)
    }

    fun onTabClicked(index: Int) {
        loadingBarActive = true
        selectedTabIndex = index

        val gridSize = tabs[index]
        val leaderboardInfos = mutableListOf<LeaderboardsInfo>()

        var requestsSent = 0
        var responsesReceived = 0
        val checkForAllResponses: () -> Unit = {
            if (responsesReceived >= requestsSent) {
                bestBoardsInfo = leaderboardInfos
//                bestBoardsInfo = List(30) { leaderboardInfos }.flatten()
                loadingBarActive = false
            }
        }

        boardManager.getBestBoardInfos(
            gridSize = gridSize,
            accountManager = accountManager,
            onSuccess = { bestBoardInfos ->

                for (boardInfo in bestBoardInfos) {
                    requestsSent++

                    accountManager.getUsername(
                        userUid = boardInfo.userUid,
                        onSuccess = { username ->

                            leaderboardInfos.add(
                                LeaderboardsInfo(
                                    boardInfo = boardInfo,
                                    username = username
                                )
                            )
                            responsesReceived++
                            checkForAllResponses()

                        },
                        onFailure = {
                            responsesReceived++
                            checkForAllResponses()
                        }
                    )
                }
                checkForAllResponses()
            },
            onFailure = {
                checkForAllResponses()
            }
        )
    }
}