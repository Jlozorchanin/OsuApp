package com.example.osuapp.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class ApiVM : ViewModel() {
    private val _tokenState: MutableStateFlow<TokenState> = MutableStateFlow(TokenState())
    val tokenState: StateFlow<TokenState> = _tokenState.asStateFlow()

    private val _userDataState : MutableStateFlow<UserDataState> = MutableStateFlow(UserDataState())
    val userDataState : StateFlow<UserDataState> = _userDataState.asStateFlow()


    fun getTokenFromCode(code: String, saveData: (AuthUser) -> Unit){
        val service = OauthApi.getInstance()
        viewModelScope.launch {
            try {
                _tokenState.value = _tokenState.value.copy(token = service.getApiKey(
                    mapOf(
                        "client_id" to Details().cliend_id,
                        "client_secret" to Details().client_secret,
                        "grant_type" to "authorization_code",
                        "code" to code,
                        "redirect_uri" to "https://clovertestcode.online/osuapp"
                    )
                ))
                saveData(_tokenState.value.token!!)

            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
    }

    fun updateDataFromDataStore(
        tokenValue : String,
        refreshTokenValue : String
    ){
        _tokenState.value = _tokenState.value.copy(
            token = AuthUser(
                access_token = tokenValue,
                expires_in = 86400,
                token_type = "meow",
                refresh_token = refreshTokenValue
            )
        )
    }

    fun updateTokenValue(refreshToken : String, saveData: (AuthUser) -> Unit){
        val service = OauthApi.getInstance()
        viewModelScope.launch {
            try {
                _tokenState.value = _tokenState.value.copy(token = service.refreshApiKey(
                    mapOf(
                        "refresh_token" to refreshToken,
                        "client_id" to Details().cliend_id,
                        "client_secret" to Details().client_secret,
                        "grant_type" to "refresh_token"
                    )
                ))
                saveData(_tokenState.value.token!!)

            } catch (e: Exception) {
                println(e.localizedMessage)
            }

        }

    }


    fun getBaseData(){
        val userService = UserApi.getInstance()

        viewModelScope.launch {
            if (_tokenState.value.token?.access_token != ""){
                for (i in 5..10){
                    try {
                        _userDataState.value = _userDataState.value.copy(data = userService.getBaseData("Bearer ${tokenState.value.token!!.access_token}"))
                        break
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                        delay(1000)
                    }
                    if (i==9) println("smt went wrong in ApiVM ")
                }

            }
            else println("нет токена, чтоб сделать запрос") //ToDo write smth normal


        }

    }
}
data class TokenState(var token: AuthUser? = null)

data class UserDataState(var data : UserData? = null)