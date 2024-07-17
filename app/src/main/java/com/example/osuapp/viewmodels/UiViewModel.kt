package com.example.osuapp.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.collection.MutableVector
import androidx.lifecycle.ViewModel
import com.example.osuapp.api.scores.ScoreItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow



class UiViewModel : ViewModel() {

    private val _uiState : MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState : StateFlow<UiState> = _uiState.asStateFlow()

    fun changeScreen(screen: Screens,currentScreen : Screens){
        _uiState.value = _uiState.value.copy(screen = screen, recentScreen = currentScreen,score = _uiState.value.score)
    }

    fun changeScore(score: ScoreItem){
        _uiState.value = _uiState.value.copy(score=score,screen = _uiState.value.screen, recentScreen = _uiState.value.recentScreen)
    }

}

data class UiState(
    val screen : Screens = Screens.HOME,
    val recentScreen : Screens = Screens.HOME,
    val score: ScoreItem? = null
)

enum class Screens {
    HOME, PROFILE, SETTINGS, WELCOME, FRIENDS, EXTRA_MAP_DETAILS, FRIEND_DETAILS
}