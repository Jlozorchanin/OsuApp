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
import com.example.osuapp.api.scores.ScoreItem
import com.example.osuapp.api.scores.Scores
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainVM : ViewModel() {
    private val _tokenState: MutableStateFlow<TokenState> = MutableStateFlow(TokenState())
    val tokenState: StateFlow<TokenState> = _tokenState.asStateFlow()

    private val _uiState : MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState : StateFlow<UiState> = _uiState.asStateFlow()

    private val _requestsState : MutableStateFlow<ReqDataState> = MutableStateFlow(ReqDataState())
    val requestsState : StateFlow<ReqDataState> = _requestsState.asStateFlow()

    private val _userDataState : MutableStateFlow<UserDataState> = MutableStateFlow(UserDataState())
    val userDataState : StateFlow<UserDataState> = _userDataState.asStateFlow()


    init { // put here calls that do not require token
        val service = MainApi.getInstance()
        viewModelScope.launch {
            try {
                _requestsState.value = _requestsState.value.copy(news = service.getNews(), friends = _requestsState.value.friends, userScores = _requestsState.value.userScores, friendScore = _requestsState.value.friendScore, friendData = _requestsState.value.friendData)
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
                        "redirect_uri" to "https://violett.tech/osuapp"
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
                        _requestsState.value = _requestsState.value.copy(friends = userService.getFriends(body = "Bearer ${tokenState.value.token!!.access_token}"), userScores = _requestsState.value.userScores,news = _requestsState.value.news, friendScore = _requestsState.value.friendScore, friendData = _requestsState.value.friendData)
                        break
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                        delay(1000)
                    }
                    if (i==9) println("smt went wrong in ApiVM - SCORES")
                }
            }
            else {
                println("wtf")
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
                        _requestsState.value = _requestsState.value.copy(userScores = userService.getUserScores(id = _userDataState.value.data?.id ?: 0, body = "Bearer ${tokenState.value.token!!.access_token}"), friends = _requestsState.value.friends,news = _requestsState.value.news, friendScore = _requestsState.value.friendScore, friendData = _requestsState.value.friendData)
                        break
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                        delay(1000)
                    }
                    if (i==9) println("smt went wrong in ApiVM - SCORES")
                }
            }
            else {
                println("wtf")
            }
        }
    }


    fun getFriendData(friendId : Int){
        if (_requestsState.value.friendData == null || friendId != _requestsState.value.friendData?.id){
            changeScreen(screen = Screens.LOADING, currentScreen = Screens.PROFILE)
            val userService = MainApi.getInstance()
            if (_tokenState.value.token?.access_token != ""){
                viewModelScope.launch {
                    try {
                        _requestsState.value = _requestsState.value.copy(friendData = userService.getFriendData(friendId, body = "Bearer ${tokenState.value.token!!.access_token}"), friendScore = userService.getUserScores(friendId,body = "Bearer ${tokenState.value.token!!.access_token}"), news = _requestsState.value.news, userScores = _requestsState.value.userScores, friends = _requestsState.value.friends)
                        changeScreen(screen = Screens.FRIEND_DETAILS, currentScreen = Screens.PROFILE)
                    } catch (e: Exception) {
                        println(e.localizedMessage)
                    }
                }
            }
        } else{
            changeScreen(screen = Screens.FRIEND_DETAILS, currentScreen = Screens.PROFILE)
        }



    }


    fun changeScreen(screen: Screens,currentScreen : Screens){
        _uiState.value = _uiState.value.copy(screen = screen, recentScreen = currentScreen,score = _uiState.value.score)
    }

    fun changeScore(score: ScoreItem){
        _uiState.value = _uiState.value.copy(score=score,screen = _uiState.value.screen, recentScreen = _uiState.value.recentScreen)
    }


}
data class TokenState(var token: AuthUser? = null)

data class UserDataState(var data : UserData? = null)

data class ReqDataState(
    var news: NewsData? = null,
    var userScores: Scores? = null,
    var friendScore: Scores? = null,
    val friendData: UserData? = null,
    var friends: Friends? = null
)

data class UiState(
    val screen : Screens = Screens.HOME,
    val recentScreen : Screens = Screens.HOME,
    val score: ScoreItem? = null
)

enum class Screens {
    HOME, PROFILE, EXTRA_MAP_DETAILS, FRIEND_DETAILS, FAQ, LOADING
}