package com.example.osuapp.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.osuapp.R
import com.example.osuapp.api.scores.ScoreItem
import com.example.osuapp.viewmodels.ReqDataState
import com.example.osuapp.viewmodels.UiState
import com.example.osuapp.viewmodels.UserDataState
import kotlin.math.roundToInt

@Composable
fun Details(
    modifier: Modifier = Modifier,
    userState: State<UserDataState>,
    reqState: State<ReqDataState>,
    score: ScoreItem ,
    back: () -> Unit,

) {
    val uriHandler = LocalUriHandler.current
    val localWidth = LocalConfiguration.current
    Box(modifier = Modifier
        .fillMaxSize()
        .paint(
            painter = painterResource(id = R.drawable.profile_bacjpeg),
            contentScale = ContentScale.Crop
        ))

    BackHandler {
        back()
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(top=4.dp, bottom = 16.dp),
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = localWidth.screenWidthDp.dp / 30)
                    .height(LocalConfiguration.current.screenHeightDp.dp / 3.5f),
                contentAlignment = Alignment.TopCenter
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            uriHandler.openUri("https://osu.ppy.sh/beatmapsets/${score.beatmapset.id}/#${score.beatmap.mode}/${score.beatmap.id}")
                        }
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp,
                                topStart = 10.dp,
                                topEnd = 10.dp
                            )
                        ),
                    model = score.beatmapset.covers.cardT,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(.5f)),
                                startY = 0f,
                                endY = 0f
                            )
                        )
                )


                Column(
                    Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp)),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start
                )
                {
                    Text(
                        modifier = Modifier.padding(start = 14.dp, end = 8.dp),
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        text = score.beatmapset.title + " от " + score.beatmapset.artist,
                        maxLines = 2,
                        fontWeight = FontWeight.SemiBold,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        modifier = Modifier.padding(start = 14.dp, end = 8.dp),
                        fontSize = 16.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        text = "CREATOR:  " + score.beatmapset.creator,
                        maxLines = 2,
                        fontWeight = FontWeight.SemiBold,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        modifier = Modifier.padding(start = 14.dp, bottom = 16.dp, end = 8.dp),
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        text = score.beatmap.version,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )


                }
            }

        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
                    .padding(horizontal = localWidth.screenWidthDp.dp / 30)
                    .height(70.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background.copy(.8f)),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    modifier = Modifier.padding(end = 8.dp, start = 14.dp),
                    text = "Created at:",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 20.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = score.created_at.replace("T","  ").dropLast(1)
                )
            }
        }
        item {
            BaseItem(title = "AR/OD/HP/DIF:", data = "${score.beatmap.ar}/${score.beatmap.accuracy}/${score.beatmap.drain}/${"%1.2f".format(score.beatmap.difficulty_rating)}" )
        }
        item { 
            BaseItem(title = "Count:", data = "${score.statistics.count_300} / ${score.statistics.count_100} / ${score.statistics.count_50} / ${score.statistics.count_miss}")
        }
        item { 
            BaseItem(title = "Max combo:", data = score.max_combo.toString()+"x")
        }
        item {
            BaseItem(
                title = "Total length:",
                data = if(score.beatmap.total_length%60 >= 10) {
                    "${score.beatmap.total_length / 60}:${score.beatmap.total_length % 60}"
                }
                 else "${score.beatmap.total_length / 60}:0${score.beatmap.total_length % 60}")
        }
    }


}

@Composable
fun BaseItem(title : String,data : String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LocalConfiguration.current.screenWidthDp.dp / 30, vertical = 8.dp)
            .height(70.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background.copy(.8f)),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier.padding(end = 8.dp, start = 14.dp),
            text = title,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontSize = 20.sp,
            maxLines = 1,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier.padding(end = 12.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = data
        )
    }
}
