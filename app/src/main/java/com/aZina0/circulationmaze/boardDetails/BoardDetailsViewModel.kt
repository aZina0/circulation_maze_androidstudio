package com.aZina0.circulationmaze.boardDetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.AccountManager
import com.aZina0.circulationmaze.BoardInfo
import com.aZina0.circulationmaze.BoardManager
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BoardDetailsViewModel @Inject constructor(
    private val boardManager: BoardManager,
    private val accountManager: AccountManager,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    var loadingBarActive by mutableStateOf(false)
        private set

    var boardUid by mutableStateOf("")
        private set
    var boardInfo by mutableStateOf<BoardInfo?>(null)
        private set

    var username by mutableStateOf("")
        private set
    var userUid by mutableStateOf("")
        private set
    var userImage by mutableStateOf<ImageBitmap?>(null)
        private set

    var boardIsShared by mutableStateOf(false)
        private set

    init {
        boardUid = savedStateHandle["boardUid"] ?: ""
        loadingBarActive = true

        boardManager.getBoardInfo(
            boardUid = boardUid,
            accountManager = accountManager,
            onSuccess = { boardInfoReturn ->
                boardInfo = boardInfoReturn

                accountManager.getAccountInfo(
                    userUid = boardInfoReturn.userUid,
                    onSuccess = { accountInfo ->
                        userUid = accountInfo.userUid
                        username = accountInfo.username
                        userImage = accountInfo.image

                        if (userUid == (Firebase.auth.currentUser?.uid ?: "")) {
                            boardManager.isBoardShared(
                                boardUid = boardUid,
                                accountManager = accountManager,
                                onSuccess = { isShared ->
                                    boardIsShared = isShared
                                    loadingBarActive = false
                                },
                                onFailure = {
                                    loadingBarActive = false
                                }
                            )
                        } else {
                            loadingBarActive = false
                        }

                    },
                    onFailure = {

                    }
                )
            },
            onFailure = {

            }
        )
    }

    fun onShareClicked() {
        loadingBarActive = true
        if (!boardIsShared) {
            boardManager.shareBoard(
                boardUid,
                accountManager = accountManager,
                onSuccess = {
                    boardIsShared = true
                    loadingBarActive = false
                },
                onFailure = {
                    loadingBarActive = false
                }
            )
        } else {
            boardManager.unshareBoard(
                boardUid,
                accountManager = accountManager,
                onSuccess = {
                    boardIsShared = false
                    loadingBarActive = false
                },
                onFailure = {
                    loadingBarActive = false
                }
            )
        }
    }
}