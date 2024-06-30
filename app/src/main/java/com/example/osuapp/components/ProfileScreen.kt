package com.example.osuapp.components

import android.content.Context
import android.content.res.AssetManager
import android.icu.text.Transliterator.Position
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.example.osuapp.R
import com.example.osuapp.viewmodels.UserDataState
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.internal.readJson
import java.io.File
import java.io.IOException
import java.io.InputStream
import kotlin.math.round
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier, userState : State<UserDataState>, back : () -> Unit, ) {
    val localWidth = LocalConfiguration.current
    val firstToolTipState = rememberBasicTooltipState()
    val secondToolTipState = rememberBasicTooltipState()
    val thirdToolTipState = rememberBasicTooltipState()

    Box(modifier = Modifier
        .fillMaxSize()
        .paint(painterResource(id = R.drawable.profile_bacjpeg), contentScale = ContentScale.Crop))
    BackHandler {
        back()
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(vertical = 16.dp )
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.TopStart
            ){
                Text(
                    text= "Основные сведения",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 26.sp,
                    modifier = Modifier.padding(start = localWidth.screenWidthDp.dp/30)
                )

            }
        }
        item {
            Box(modifier = Modifier
                .padding(horizontal = localWidth.screenWidthDp.dp / 30, vertical = 16.dp)
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.CenterStart)
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.8f)
                        .padding(start = 16.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(modifier = Modifier.padding(end = 16.dp),tint = MaterialTheme.colorScheme.onSecondaryContainer,painter = painterResource(id = R.drawable.crown), contentDescription = null)
                    Text(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = "#${userState.value.data?.statistics?.global_rank.toString()}   //   #${userState.value.data?.statistics?.country_rank.toString()}"
                    )
                }
            }

        }

        item {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(horizontal = localWidth.screenWidthDp.dp / 30)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.CenterStart)
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(.8f)
                        .padding(start = 16.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    BasicTooltipBox(
                        tooltip = { Text("PP - performances point") },
                        state = firstToolTipState,
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()
                    ){
                        Text(
                            modifier = Modifier.padding(end = 8.dp),
                            text = "PP",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 22.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold

                        )
                    }

                    Text(
                        modifier = Modifier.padding(end = 12.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = userState.value.data?.statistics?.pp?.roundToInt().toString()
                    )
                    BasicTooltipBox(
                        tooltip = { Text("PT - play time") },
                        state = secondToolTipState,
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()
                    ){
                        Text(
                            modifier = Modifier.padding(end = 8.dp),
                            text = "PT",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 22.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold

                        )
                    }
                    Text(
                        modifier = Modifier.padding(end = 12.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = ("${(userState.value.data?.statistics?.play_time?.div(3600)) ?: 0}h").toString()
                    )
                    BasicTooltipBox(
                        tooltip = { Text("ACC - Accuracy / Точность") },
                        state = thirdToolTipState,
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider()
                    ){
                        Text(
                            modifier = Modifier.padding(end = 8.dp),
                            text = "ACC",
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 22.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold

                        )
                    }
                    Text(
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        text = "%1.1f".format(userState.value.data?.statistics?.hit_accuracy?: 0.1) + "%"
                    )
                }
            }

        }

    }


    // ToDo Это просто очень большая хотелка, но сделай историю рейтинга и плейтайм как PATH

    

}



