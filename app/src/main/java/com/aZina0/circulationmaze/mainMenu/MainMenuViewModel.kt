package com.aZina0.circulationmaze.mainMenu

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.aZina0.circulationmaze.Global
import com.google.firebase.auth.FirebaseAuth

class MainMenuViewModel : ViewModel() {
    var userLoggedIn by mutableStateOf(false)
        private set

    init {
        Global.print("MAIN MENU VIEWMODEL INIT")
        val user = FirebaseAuth.getInstance().currentUser
        userLoggedIn = user != null
    }
}