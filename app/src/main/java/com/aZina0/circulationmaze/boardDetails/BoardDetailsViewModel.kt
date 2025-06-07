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

    var boardInfo by mutableStateOf<BoardInfo?>(null)
        private set

    var username by mutableStateOf("")
        private set
    var userImage by mutableStateOf<ImageBitmap?>(null)
        private set

    init {
        val boardUid = savedStateHandle["boardUid"] ?: ""

        boardManager.getBoardInfo(
            boardUid = boardUid,
            accountManager = accountManager,
            onSuccess = { boardInfoReturn ->
                boardInfo = boardInfoReturn

                accountManager.getAccountInfo(
                    userUid = boardInfoReturn.userUid,
                    onSuccess = { accountInfo ->
                        username = accountInfo.username
                        userImage = accountInfo.image
                    },
                    onFailure = {

                    }
                )
            },
            onFailure = {

            }
        )

    }
}