package com.example.osuapp.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ProfileScreen(modifier: Modifier = Modifier,back : () -> Unit) {
    BackHandler {
        back()
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(text = "la")
    }
}