package com.example.osuapp

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.compose.OsuAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            OsuAppTheme(darkTheme = true) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TestScreen(modifier: Modifier = Modifier) {
    Box(modifier =modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Column(modifier = Modifier
            .fillMaxSize()
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Box(modifier = Modifier.padding(16.dp).size(140.dp,100.dp).clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primary)
            )
            Box(modifier = Modifier.padding(16.dp).size(140.dp,100.dp).clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.secondary)
            )
            Box(modifier = Modifier.padding(16.dp).size(140.dp,100.dp).clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.tertiary)
            )

        }
    }
}

