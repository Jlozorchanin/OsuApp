package com.example.osuapp.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BasicTooltipBox
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberBasicTooltipState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.osuapp.R
import com.example.osuapp.api.friends.FriendsItem
import com.example.osuapp.api.scores.ScoreItem
import com.example.osuapp.graph.ChartStyle
import com.example.osuapp.graph.DataPoint
import com.example.osuapp.graph.LineGraph
import com.example.osuapp.viewmodels.ReqDataState
import com.example.osuapp.viewmodels.UserDataState
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userState: State<UserDataState>,
    reqState: State<ReqDataState>,
    lazyListState: LazyListState,
    expanded : Boolean,
    back: () -> Unit,
    changeExpanded : () -> Unit,
    openDetails: (ScoreItem) -> Unit,
    openFriendDetail : (friendId:Int) -> Unit
) {
    val localWidth = LocalConfiguration.current
    val firstToolTipState = rememberBasicTooltipState()
    val secondToolTipState = rememberBasicTooltipState()
    val thirdToolTipState = rememberBasicTooltipState()
    val dataPoints by remember(userState.value.data) {
        mutableStateOf(
            userState.value.data?.rankHistory?.data?.mapIndexed { index, result ->
                DataPoint(
                    x = index.toFloat(),
                    y = - result.toFloat(),
                    xLabel = result.toString()
                )
            } ?: emptyList<DataPoint>()
        )
    }
    var selectedDataPoint by remember {
        mutableStateOf<DataPoint?>(dataPoints.last())
    }
    var labelWidth by remember {
        mutableFloatStateOf(0f)
    }
    var totalChartWidth by remember {
        mutableFloatStateOf(0f)
    }
    val amountOfVisibleDataPoints = if(labelWidth > 0){
        ((totalChartWidth - 2.5 * labelWidth) / labelWidth).toInt()
    } else 0



    Box(modifier = Modifier
        .fillMaxSize()
        .paint(painterResource(id = R.drawable.profile_bacjpeg), contentScale = ContentScale.Crop))
    BackHandler {
        back()
    }

    LazyColumn(
        state = lazyListState,
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(vertical = 0.dp ),
    ) {

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
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
                .background(MaterialTheme.colorScheme.primaryContainer.copy(.85f)),
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
                .background(MaterialTheme.colorScheme.primaryContainer.copy(.85f)),
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
                        tooltip = { Text("    PP - performances point") },
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




        item{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end =12.dp, start = 12.dp,top = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        MaterialTheme.colorScheme.background.copy(.7f)
                    )


                ,
                contentAlignment = Alignment.CenterStart
            ){
                LineGraph(
                    dataPoints = dataPoints,
                    style = ChartStyle(
                        chartLineColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.secondary,
                        selectedColor = MaterialTheme.colorScheme.primary,
                        helperLinesThickness = 5f,
                        axisLinesThickness = 5f,
                        labelFontSize = 14.sp,
                        minYLabelSpacing = 20.dp,
                        verticalPadding = 8.dp,
                        horizontalPadding = 8.dp,
                        xAxisLabelSpacing = 8.dp,
                    ),
                    visibleDataPointsIndices = 1..89,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(16/9f)
                        .onSizeChanged { totalChartWidth = it.width.toFloat() }
                        .padding(end = 28.dp)
                        .offset((-10).dp)

                    ,
                    selectedDataPoint = selectedDataPoint,
                    onSelectedDataPoint = {selectedDataPoint = it},
                    onXLabelWidthChange = {
                        labelWidth = it
                    }
                )

            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                ,
                contentAlignment = Alignment.CenterStart
            ){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text= "Рекорды",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 26.sp,
                        modifier = Modifier.padding(start = localWidth.screenWidthDp.dp/30, top = 16.dp)
                    )
                    IconButton(modifier = Modifier
                        .size(100.dp)
                        .padding(top = 18.dp)
                        .offset(x = (-30).dp), onClick = {changeExpanded()}){
                        Icon(if (!expanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp, contentDescription = null)
                    }
                }


            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item{
            AnimatedContent(
                targetState = expanded,
                transitionSpec = {
                    fadeIn(animationSpec = tween(150, 150)) togetherWith
                            fadeOut(animationSpec = tween(150)) using
                            SizeTransform { initialSize, targetSize ->
                                if (targetState) {
                                    keyframes {
                                        // Expand horizontally first.
                                        IntSize(targetSize.width, initialSize.height) at 150
                                        durationMillis = 300
                                    }
                                } else {
                                    keyframes {
                                        // Shrink vertically first.
                                        IntSize(initialSize.width, targetSize.height) at 150
                                        durationMillis = 300
                                    }
                                }
                            }
                }, label = ""
            ) { targetExpanded ->
                if (targetExpanded) {
                    Column {
                        for (i in reqState.value.userScores?: emptyList()){
                            MapItem(score = i) {
                                openDetails(i)
                            }
                        }
                    }
                } else {
                    reqState.value.userScores?.get(0)?.let {
                        MapItem(score = it) {
                            openDetails(it)
                        }
                    }
                }
            }

        }


//        items(reqState.value.userScores?: emptyList()){ score ->
//            MapItem(score = score){
//                openDetails(score)
//            }
//        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.TopStart
            ){
                Text(
                    text= "Друзья",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 26.sp,
                    modifier = Modifier.padding(start = localWidth.screenWidthDp.dp/30, top = 16.dp)
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(reqState.value.friends?: emptyList()){ friend ->
            FriendItem(friend = friend) {
                openFriendDetail(friend.id)
            }
        }
        
        


    }


    // ToDo Это просто очень большая хотелка, но сделай историю рейтинга и плейтайм как PATH

    

}

@Composable
fun FriendItem(modifier: Modifier = Modifier,friend : FriendsItem,openDetails : () -> Unit) {
    val uriHandler = LocalUriHandler.current
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(
            start = LocalConfiguration.current.screenWidthDp.dp / 30,
            end = LocalConfiguration.current.screenWidthDp.dp / 30,
            bottom = 16.dp,
        )
        .height(70.dp)
        .clip(
            RoundedCornerShape(
                topStart = 32.dp,
                bottomStart = 32.dp,
                bottomEnd = 16.dp,
                topEnd = 16.dp
            )
        ),
        contentAlignment = Alignment.CenterStart)
    {
        Row(
            Modifier
                .fillMaxSize()
                .clickable {
                    openDetails()
                }
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically,
            )
        {
            AsyncImage(
                modifier = Modifier.clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)),
                model = friend.avatar_url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Text(
                modifier = Modifier.padding(start = 14.dp),
                text = friend.username,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 18.sp,
                maxLines = 1,
                fontWeight = FontWeight.Bold
            )
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd){
                Text(
                    modifier = Modifier.padding(end = 14.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = "#" +friend.statistics.global_rank.toString(),
                )
            }




        }
    }
}

@Composable
fun MapItem(modifier: Modifier = Modifier,score : ScoreItem,openDetails : () -> Unit) {
    val uriHandler = LocalUriHandler.current
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(
            start = LocalConfiguration.current.screenWidthDp.dp / 30,
            end = LocalConfiguration.current.screenWidthDp.dp / 30,
            bottom = 20.dp
        )
        .height(160.dp)
        .clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.CenterStart)
    {

        AsyncImage(modifier = Modifier.fillMaxSize(),model = score.beatmapset.covers.cardT, contentScale = ContentScale.Crop, contentDescription = null)
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable {
                openDetails()
//                uriHandler.openUri("https://osu.ppy.sh/beatmapsets/${score.beatmapset.id}/#${score.beatmap.mode}/${score.beatmap.id}")
            }
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black.copy(.5f)),
                    startY = 0f,
                    endY = 0f
                )
            ))
        Column(
            modifier
                .fillMaxSize()
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start)
        {
            Text(modifier = Modifier.padding(start = 14.dp,end = 8.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                text = score.beatmapset.title + " от " + score.beatmapset.artist,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis
            )
            Text(modifier = Modifier.padding(start = 14.dp, bottom = 8.dp, end = 8.dp),
                fontSize = 14.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                text = score.beatmap.version,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,)

        }

        Column(
            modifier
                .fillMaxSize()
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start)
        {
            Text(modifier = Modifier.padding(start = 14.dp,end = 8.dp,top=10.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,

                text = "PP: " + score.pp.roundToInt().toString() + "   ACC: ${"%1.1f".format(score.accuracy*100)}%",
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis
            )
            if (score.mods.isNotEmpty())
            Text(modifier = Modifier.padding(start = 14.dp,end = 8.dp,top=10.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                text = "MODS:  " + score.mods.toString().drop(1).removeSuffix("]"),
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis
            )


        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd){
            Text(modifier = Modifier.padding(end = 14.dp),
                fontSize = 50.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                text = score.rank,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,)
        }

    }
}



