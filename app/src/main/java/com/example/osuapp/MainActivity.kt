package com.example.osuapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
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

    @OptIn(ExperimentalMaterial3Api::class)
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
                    var isFirstLaunch by remember {
                        mutableStateOf(true)
                    }

                    LaunchedEffect(key1 = true) {
                        isFirstLaunch = provider.getInfo.first()
                        val tokenTime = provider.getTokenDate.first()
                        if (currentDate.toInt() - tokenTime.toString().toInt() > 86000 ){
                            apiVM.authUser{
                                scope.launch {
                                    provider.saveTokenAndTime(currentDate.toInt(),it.access_token).also {
                                        tokenValue.value.token?.access_token = provider.getToken.first().also {
                                            apiVM.getBaseData("29269502")


                                        }
                                    }
                                }

                            }

                        }
                        else{
                            scope.launch {
                                apiVM.updateTokenValue(provider.getToken.first()).also {
                                    apiVM.getBaseData("29269502")
                                }
                            }
                        }


                    }

                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(title = {
                                AnimatedVisibility(visible = mainState.value.data?.username != null,
                                    enter = fadeIn(tween(200))) {

                                    Text(text = mainState.value.data?.username ?: "", maxLines = 1, overflow = TextOverflow.Clip)
                                }
                            },

                                navigationIcon = {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Filled.Menu,
                                            contentDescription = null
                                        )
                                    }
                                },
                                actions = {
                                    AnimatedVisibility(visible = mainState.value.data?.avatar_url != null,
                                        enter = fadeIn(tween(200))
                                    ){
                                        AsyncImage(
                                            modifier = Modifier
                                                .padding(end = 12.dp)
                                                .clip(RoundedCornerShape(50.dp))
                                                .size(55.dp),
                                            model = mainState.value.data?.avatar_url ?: "",
                                            contentDescription = null
                                        )
                                    }

                                }
                            )
                        },
                        content = { innerPadding ->
                        MainScreen(modifier = Modifier.padding(innerPadding), state = mainState)
                        }
                    )

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
fun MainScreen(modifier: Modifier = Modifier, state: State<UserDataState>) {

    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)){

    }
    
}





