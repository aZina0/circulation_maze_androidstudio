package com.aZina0.circulationmaze.userList

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.AccountInfo
import com.aZina0.circulationmaze.AccountManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val accountManager: AccountManager,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    var loadingBarActive by mutableStateOf(false)
        private set
    var title by mutableStateOf("")

    var userList by mutableStateOf(emptyList<AccountInfo>())

    fun onStart() {
        val type = savedStateHandle["type"] ?: ""
        val userUid = savedStateHandle["userUid"] ?: ""

        loadingBarActive = true
        when (type) {
            "followers" -> {
                title = "Followers"
                accountManager.getFollowers(
                    userUid = userUid,
                    onSuccess = {
                        userList = it
                        loadingBarActive = false
                    },
                    onFailure = {
                        loadingBarActive = false
                    }
                )
            }
            "following" -> {
                title = "Following"
                accountManager.getFollowing(
                    userUid = userUid,
                    onSuccess = {
                        userList = it
                        loadingBarActive = false
                    },
                    onFailure = {
                        loadingBarActive = false
                    }
                )
            }
        }

    }
}