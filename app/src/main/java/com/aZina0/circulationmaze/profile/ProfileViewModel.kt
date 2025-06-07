package com.aZina0.circulationmaze.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.AccountManager
import com.aZina0.circulationmaze.BoardManager
import com.aZina0.circulationmaze.BoardSmallInfo
import com.aZina0.circulationmaze.SaveManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val accountManager: AccountManager,
    private val saveManager: SaveManager,
    private val boardManager: BoardManager,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    var loadingBarActive by mutableStateOf(false)
        private set
    var userUid by mutableStateOf("")
        private set
    var picture: ImageBitmap by mutableStateOf(accountManager.defaultProfileImage)
        private set
    var username by mutableStateOf("")
        private set
    var dateJoined: LocalDateTime by mutableStateOf(LocalDateTime.MIN)
        private set

    var userFollowed by mutableStateOf(false)
        private set

    var description by mutableStateOf("")
        private set

    var level by mutableIntStateOf(0)
        private set
    var xpRemainder by mutableFloatStateOf(0f)
        private set

    var allSharedBoardsSmallInfo by mutableStateOf(listOf<BoardSmallInfo>())
        private set
    var allSolvedBoardsSmallInfo by mutableStateOf(listOf<BoardSmallInfo>())
        private set

    init {
        userUid = savedStateHandle["userUid"] ?: ""
        val highlightAllSolvedBoards = savedStateHandle["highlightAllSolvedBoards"] ?: false

        loadingBarActive = true

        var requestsSent = 0
        var responsesReceived = 0
        val checkForAllResponses: () -> Unit = {
            if (responsesReceived >= requestsSent) {
                loadingBarActive = false
            }
        }

        requestsSent++
        accountManager.getAccountInfo(
            userUid = userUid,
            onSuccess = { accountInfo ->
                picture = accountInfo.image
                username = accountInfo.username
                description = accountInfo.description

                val levelAndRemainder = accountManager.getLevelAndRemainder(accountInfo.xp)
                level = levelAndRemainder.first
                xpRemainder = levelAndRemainder.second
                responsesReceived++
                checkForAllResponses()
            },
            onFailure = {
                responsesReceived++
                checkForAllResponses()
            }
        )

        dateJoined = Instant.ofEpochMilli(Firebase.auth.currentUser!!.metadata!!.creationTimestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        requestsSent++
        accountManager.checkIfFollowed(
            userUidFollowing = Firebase.auth.currentUser?.uid ?: "",
            userUidFollowed = userUid,
            onSuccess = { result ->
                userFollowed = result
                responsesReceived++
                checkForAllResponses()
            },
            onFailure = {
                responsesReceived++
                checkForAllResponses()
            }
        )

        requestsSent++
        boardManager.getAllSolvedBoards(
            userUid = userUid,
            accountManager = accountManager,
            onSuccess = {
                allSolvedBoardsSmallInfo = it
                responsesReceived++
                checkForAllResponses()
            },
            onFailure = {
                responsesReceived++
                checkForAllResponses()
            }
        )

        requestsSent++
        boardManager.getSharedBoards(
            userUid = userUid,
            accountManager = accountManager,
            onSuccess = {
                allSharedBoardsSmallInfo = it
                responsesReceived++
                checkForAllResponses()
            },
            onFailure = {
                responsesReceived++
                checkForAllResponses()
            }
        )
    }

    fun onLogoutClick() {
        accountManager.signOut(saveManager)
    }

    fun onFollowClicked() {
        val userUidFollowing = Firebase.auth.currentUser?.uid ?: ""
        val userUidFollowed = userUid

        if (!userFollowed) {
            accountManager.followUser(
                userUidFollowing = userUidFollowing,
                userUidFollowed = userUidFollowed,
                onSuccess = {
                    userFollowed = true
                },
                onFailure = {}
            )
        } else {
            accountManager.unfollowUser(
                userUidFollowing = userUidFollowing,
                userUidFollowed = userUidFollowed,
                onSuccess = {
                    userFollowed = false
                },
                onFailure = {}
            )
        }
    }
}