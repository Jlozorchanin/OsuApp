package com.example.osuapp.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ApiVM : ViewModel() {
    private val _tokenState: MutableStateFlow<TokenState> = MutableStateFlow(TokenState())
    val tokenState: StateFlow<TokenState> = _tokenState

    private val _userDataState : MutableStateFlow<UserDataState> = MutableStateFlow(UserDataState())
    val userDataState : StateFlow<UserDataState> = _userDataState
    fun authUser() {
        val service = OauthApi.getInstance()
        viewModelScope.launch {
            try {
                _tokenState.value.token = service.getApiKey()
                println(_tokenState.value.token)

            } catch (e: Exception) {
                println(e.localizedMessage)
            }

        }

    }

    fun updateTokenInfo(token : AuthUser){
        _tokenState.value.token = token
    }

    fun getBaseData(token: String, userId: String) {
        val userService = UserApi.getInstance()
        viewModelScope.launch {
            try {
                _userDataState.value.data = userService.getBaseData("Bearer $token", userId = userId)

            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }


    }
}
data class TokenState(var token: AuthUser? = null)

data class UserDataState(var data : UserData? = null)