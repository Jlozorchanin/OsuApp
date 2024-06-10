package com.example.osuapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.compose.OsuAppTheme
import com.example.osuapp.screens.WelcomeScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )


        setContent {
            OsuAppTheme(darkTheme = true) {
                Surface {
                    val provider = DataStoreProvider(LocalContext.current)
                    val isFirstLaunch = provider.getInfo.collectAsState(initial = true).value
                    val scope = rememberCoroutineScope()
                    if (isFirstLaunch){
                        WelcomeScreen{scope.launch{provider.saveInfo(false)}}
                    } else{
                        MainScreen()
                    }
                }
            }
        }
    }


}



@Composable
fun MainScreen(modifier: Modifier = Modifier) {



}


class DataStoreProvider(private val context : Context){
    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        private val first_launch = booleanPreferencesKey("isFirstLaunch")
    }

    val getInfo : Flow<Boolean> = context.dataStore.data.map {
        it[first_launch] ?: true
    }

    suspend fun saveInfo(value : Boolean){
        context.dataStore.edit {
            it[first_launch] = value
        }
    }
}
