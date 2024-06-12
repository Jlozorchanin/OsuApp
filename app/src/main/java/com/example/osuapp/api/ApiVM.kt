package com.example.osuapp.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ApiVM : ViewModel() {
    private val _tokenState: MutableStateFlow<TokenState> = MutableStateFlow(TokenState())
    val tokenState: StateFlow<TokenState> = _tokenState

    private val _userDataState : MutableStateFlow<UserDataState> = MutableStateFlow(UserDataState())
    val userDataState : StateFlow<UserDataState> = _userDataState
    fun authUser(saveData : (AuthUser) -> Unit) {
        val service = OauthApi.getInstance()
        viewModelScope.launch {
            try {
                _tokenState.value = _tokenState.value.copy(service.getApiKey())
                saveData(_tokenState.value.token!!)

            } catch (e: Exception) {
                println(e.localizedMessage)
            }

        }

    }

    fun updateTokenInfo(token : AuthUser){
        _tokenState.value.token = token
    }

    fun getBaseData(token: String, userId: String){
        val userService = UserApi.getInstance()

        viewModelScope.launch {
            try {
                _userDataState.value = _userDataState.value.copy(userService.getBaseData("Bearer $token", userId = userId))
                println(_userDataState.value.data!!.avatar_url)
            } catch (e: Exception) {
                println(e.localizedMessage)
            }

        }

    }
}
data class TokenState(var token: AuthUser? = null)

data class UserDataState(var data : UserData? = null)