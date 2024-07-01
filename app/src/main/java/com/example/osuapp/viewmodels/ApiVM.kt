package com.example.osuapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.osuapp.api.AuthUser
import com.example.osuapp.api.Details
import com.example.osuapp.api.MainApi
import com.example.osuapp.api.OauthApi
import com.example.osuapp.api.UserData
import com.example.osuapp.api.friends.Friends
import com.example.osuapp.api.news.NewsData
import com.example.osuapp.api.scores.Beatmap
import com.example.osuapp.api.scores.Scores
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ApiVM : ViewModel() {
    private val _tokenState: MutableStateFlow<TokenState> = MutableStateFlow(TokenState())
    val tokenState: StateFlow<TokenState> = _tokenState.asStateFlow()

    private val _requestsState : MutableStateFlow<ReqDataState> = MutableStateFlow(ReqDataState())
    val requestsState : StateFlow<ReqDataState> = _requestsState.asStateFlow()

    private val _userDataState : MutableStateFlow<UserDataState> = MutableStateFlow(UserDataState())
    val userDataState : StateFlow<UserDataState> = _userDataState.asStateFlow()


    init { // put here calls that do not require token
        val service = MainApi.getInstance()
        viewModelScope.launch {
            try {
                _requestsState.value = _requestsState.value.copy(news = service.getNews(), friends = _requestsState.value.friends, userScores = _requestsState.value.userScores, friendsScores = _requestsState.value.friendsScores,)
            }
            catch (e:Exception){
                println(e.localizedMessage)
            }
        }
    }

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

    fun getFriends(){
        val userService = MainApi.getInstance()
        viewModelScope.launch {
            if (_tokenState.value.token?.access_token != ""){
                for (i in 5..10){
                    try {
                        _requestsState.value = _requestsState.value.copy(friends = userService.getFriends(body = "Bearer ${tokenState.value.token!!.access_token}"), userScores = _requestsState.value.userScores,news = _requestsState.value.news, friendsScores = _requestsState.value.friendsScores,)
                        break
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                        delay(1000)
                    }
                    if (i==9) println("smt went wrong in ApiVM - SCORES")
                }
            }
            else {
                println("bugs BRO")
            }
        }
    }

    fun getBaseData(){
        val userService = MainApi.getInstance()
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
                    if (i==9) println("smt went wrong in ApiVM - BASEDATA")
                }
            }
        }
    }

    fun getScores(){
        val userService = MainApi.getInstance()
        viewModelScope.launch {
            if (_tokenState.value.token?.access_token != ""){
                for (i in 5..10){
                    try {
                        _requestsState.value = _requestsState.value.copy(userScores = userService.getUserScores(id = _userDataState.value.data?.id ?: 0, body = "Bearer ${tokenState.value.token!!.access_token}"), friends = _requestsState.value.friends,news = _requestsState.value.news, friendsScores = _requestsState.value.friendsScores,)
                        break
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                        delay(1000)
                    }
                    if (i==9) println("smt went wrong in ApiVM - SCORES")
                }
            }
            else {
                println("bugs BRO")
            }
        }
    }
}
data class TokenState(var token: AuthUser? = null)

data class UserDataState(var data : UserData? = null)

data class ReqDataState(
    var news: NewsData? = null,
    var userScores: Scores? = null,
    var friendsScores: MutableList<Scores>? = null,
    var friends: Friends? = null
)