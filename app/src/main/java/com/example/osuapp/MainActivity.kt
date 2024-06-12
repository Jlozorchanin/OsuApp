package com.example.osuapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.request.ImageResult
import com.example.compose.OsuAppTheme
import com.example.osuapp.api.ApiVM
import com.example.osuapp.api.AuthUser
import com.example.osuapp.api.UserDataState
import com.example.osuapp.screens.WelcomeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )


        setContent {
            OsuAppTheme(darkTheme = true) {
                val apiVM by viewModels<ApiVM>()
                Surface {
                    val provider = DataStoreProvider(LocalContext.current)
                    val currentDate = System.currentTimeMillis()/1000
                    val scope = rememberCoroutineScope()
                    val tokenValue = apiVM.tokenState.collectAsState()
                    val mainState = apiVM.userDataState.collectAsState()
                    var image by remember {
                        mutableStateOf(mainState.value)
                    }
                    var isFirstLaunch by remember {
                        mutableStateOf(true)
                    }
                    var token by remember{
                        mutableStateOf("")
                    }
                    
                    LaunchedEffect(key1 = true) {
                        isFirstLaunch = provider.getInfo.first()
                        val tokenTime = provider.getTokenDate.first()

                        if (currentDate.toInt() - tokenTime.toString().toInt() > 86000 ){
                            apiVM.authUser{
                                scope.launch {
                                    provider.saveTokenAndTime(currentDate.toInt(),it.access_token)
                                    println("test1")
                                }

                            }


                        }
                        token = provider.getToken.first()
                        apiVM.updateTokenInfo(AuthUser(
                            access_token = token?: "",
                            expires_in = currentDate.toInt() - tokenTime.toString().toInt(),
                            token_type = ""
                        ))
                        apiVM.getBaseData(token,"29269502")

                    }

                    MainScreen(modifier = Modifier,image = image.data?.avatar_url)
                    if(isFirstLaunch) WelcomeScreen{
                        scope.launch{provider.saveInfo(false)
                        }
                        isFirstLaunch = false

                    }


                }
            }
        }
    }


}



@Composable
fun MainScreen(modifier: Modifier = Modifier,image : String?) {

        Column(modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            AsyncImage(
                image,"",modifier = Modifier
//                        state.value.data?.avatar_url,"",modifier = Modifier
                    .padding(16.dp)
                    .size(150.dp)
                    .clip(RoundedCornerShape(16.dp)))

        }

    }





