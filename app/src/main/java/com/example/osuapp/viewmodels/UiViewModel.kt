package com.example.osuapp.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.collection.MutableVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow



class UiViewModel : ViewModel() {

    private val _uiState : MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState : StateFlow<UiState> = _uiState.asStateFlow()

    fun changeScreen(screen: Screens){
        _uiState.value = _uiState.value.copy(screen = screen)
    }

}

data class UiState(
    val screen : Screens = Screens.HOME
)

enum class Screens {
    HOME, PROFILE, SETTINGS, WELCOME
}