package com.example.osuapp.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthVM : ViewModel() {
    private val _tokenState : MutableStateFlow<TokenState> = MutableStateFlow(TokenState())
    val tokenState : StateFlow<TokenState> = _tokenState
    fun authUser(){
        val service = OauthApi.getInstance()
        viewModelScope.launch {
            try {
                _tokenState.value.token = service.getApiKey()
                println(_tokenState.value.token)
            }
            catch (e: Exception){
                println(e.localizedMessage)
            }
        }

    }
}
data class TokenState(var token: AuthUser? = null)