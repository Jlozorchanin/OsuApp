package com.example.osuapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.osuapp.R

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier, launchSettings : () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val uri = "https://osu.ppy.sh/oauth/authorize?client_id=32490&redirect_uri=https://clovertestcode.online/osuapp&response_type=code&scope=public+identify+friends.read"
    
    Box(modifier = Modifier
        .fillMaxSize()
        .paint(
            painterResource(id = R.drawable.welcome_screen),
            contentScale = ContentScale.FillBounds
        ))
    
    Column(modifier = modifier
        .fillMaxSize()
        .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
                
        
            Text(modifier = Modifier.padding(18.dp),
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                text = "Для продолжения работы приложения вам необходимо авторизоваться через свой Osu аккаунт"
            )

            Button(
                onClick = {
                uriHandler.openUri(uri)
                },
                colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ))
            {
                Text(text = "Авторизоваться", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

    }
}



