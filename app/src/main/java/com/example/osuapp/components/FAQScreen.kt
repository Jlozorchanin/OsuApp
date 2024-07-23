package com.example.osuapp.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.osuapp.R

@Composable
fun FAQScreen(modifier: Modifier = Modifier,back : () -> Unit, launchSettings : () ->Unit) {
    BackHandler {
        back()
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .paint(
            painterResource(id = R.drawable.welcome_screen),
            contentScale = ContentScale.FillBounds
        ))

        Column(modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(modifier = Modifier.padding(horizontal = 10.dp), fontSize = 19.sp, text = "Перед вами приложение посвященное такой мобильной игре как OSU!  Для работы приложения необходимо авторизоваться под своей учетной записью\n" +
                    "\nДля просмотра большей части контента приложения нажмите на аватарку пользователя",textAlign = TextAlign.Center )
            Spacer(modifier = Modifier.height(20.dp))



            Text(fontSize = 19.sp , text = "В случае, если не происходит перенаправление обратно в приложение, нажмите на кнопку ниже и настройте открытие ссылок по умолчанию в \"Открывать по умолчанию\"", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { launchSettings() }) {
                Text(text = "Открыть настройки", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
    }
}