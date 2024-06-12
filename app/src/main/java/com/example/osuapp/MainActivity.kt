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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.compose.OsuAppTheme
import com.example.osuapp.api.ApiVM
import com.example.osuapp.api.AuthUser
import com.example.osuapp.screens.WelcomeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val apiVM by viewModels<ApiVM>()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )


        setContent {
            OsuAppTheme(darkTheme = true) {
                Surface {
                    val provider = DataStoreProvider(LocalContext.current)
                    val currentDate = System.currentTimeMillis()/1000
                    val scope = rememberCoroutineScope()
                    val tokenValue = apiVM.tokenState.collectAsState()
                    val mainState = apiVM.userDataState.collectAsState()
                    var isFirstLaunch by remember {
                        mutableStateOf(true)
                    }
                    var token by remember{
                        mutableStateOf(tokenValue.value.token?.access_token)
                    }
                    
                    LaunchedEffect(key1 = true) {
                        isFirstLaunch = provider.getInfo.first()
                        delay(1000)
                        val tokenTime = provider.getTokenDate.first()
                        println(tokenTime)
                        println(currentDate.toInt())
                        if (currentDate.toInt() - tokenTime.toString().toInt() > 86000 ){
                            val tokenData = apiVM.authUser()
                            println(tokenData)
                            provider.saveTokenAndTime(currentDate.toInt(),tokenData)
                        }
                        token = provider.getToken.first()

                        delay(1000)
                        apiVM.updateTokenInfo(AuthUser(
                            access_token = token?: "",
                            expires_in = currentDate.toInt() - tokenTime.toString().toInt(),
                            token_type = ""
                        ))




                    }

                    MainScreen(modifier = Modifier,
                        mainState.value.data?.avatar_url ?: ""
                    ){
                        println(apiVM.tokenState.value.token)
                        tokenValue.value.token?.let { apiVM.getBaseData(it.access_token,"29269502") }

                    }
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
fun MainScreen( modifier: Modifier = Modifier,image:String?, loadImage : () -> Unit) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center){
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            AsyncImage(image,"",modifier = Modifier
                .padding(16.dp)
                .size(150.dp)
                .clip(RoundedCornerShape(16.dp)))
            Button(onClick = { loadImage() }) {
                Text(text = "Load profile image")
            }
        }

    }
}





