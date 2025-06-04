package com.aZina0.circulationmaze.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.AccountManager
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
): ViewModel() {
    var picture: ImageBitmap? = null
        private set
    var username by mutableStateOf("")
        private set
    var level by mutableIntStateOf(0)
        private set
    var xpRemainder by mutableFloatStateOf(0f)
        private set
    var dateJoined: LocalDateTime by mutableStateOf(LocalDateTime.MIN)
        private set
    var loadingBarActive by mutableStateOf(false)
        private set

    init {
        loadingBarActive = true

        accountManager.getUsername(
            onSuccess = {
                username = it
            },
            onFailure = {},
        )

        dateJoined = Instant.ofEpochMilli(Firebase.auth.currentUser!!.metadata!!.creationTimestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        accountManager.getXp(
            onSuccess = {
                val levelAndRemainder = accountManager.getLevelAndRemainder(it)
                level = levelAndRemainder.first
                xpRemainder = levelAndRemainder.second
            },
            onFailure = {},
        )

        loadingBarActive = false
    }

    fun onLogoutClick() {
        Firebase.auth.signOut()
    }
}