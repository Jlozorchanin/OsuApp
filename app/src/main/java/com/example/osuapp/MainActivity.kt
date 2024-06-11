package com.example.osuapp

import android.content.Context
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.compose.OsuAppTheme
import com.example.osuapp.api.AuthUser
import com.example.osuapp.api.AuthVM
import com.example.osuapp.screens.WelcomeScreen
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll
import java.text.SimpleDateFormat
import java.time.Clock

class MainActivity : ComponentActivity() {
    private val authVM by viewModels<AuthVM>()
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
                    val tokenValue = authVM.tokenState.collectAsState()
                    val isFirstLaunch = remember {
                        mutableStateOf(false)
                    }
                    LaunchedEffect(key1 = true) {
                        provider.getInfo.collect{
                            isFirstLaunch.value = it
                        }
                        val tokenTime = provider.getTokenDate.first()
                        if ((currentDate.toInt() - tokenTime.toString().toInt()) > 86000 ){
                            authVM.authUser()
                        }
                        tokenValue.value.token?.access_token?.let {
                            provider.saveTokenAndTime(currentDate.toInt(),
                                it
                            )
                        }

                    }
                    MainScreen()
                    if(isFirstLaunch.value) WelcomeScreen{scope.launch{provider.saveInfo(false)}}



                    val token = provider.getToken.collectAsState(initial = true).value
                    println(token)


                }
            }
        }
    }


}



@Composable
fun MainScreen( modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background))
}





