package com.example.osuapp.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier, finishValidation : () -> Unit) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Button(onClick = { finishValidation()}) {
            Text(text = "Finish validation")
        }

    }
}