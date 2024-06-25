package com.example.osuapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.osuapp.api.news.NewsPost

@Composable
fun NewsItem(modifier: Modifier = Modifier,post: NewsPost,onClick : (String) -> Unit) {
    val localWidth = LocalConfiguration.current
    Box(modifier = modifier

        .fillMaxWidth()
        .padding(
            bottom = localWidth.screenHeightDp.dp / 40,
            start = localWidth.screenWidthDp.dp / 42,
            end = localWidth.screenWidthDp.dp / 45,
        )
        .wrapContentWidth(Alignment.Start)
        .widthIn(300.dp, 900.dp)
        .heightIn(140.dp, 200.dp))
    {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(28.dp)),
            contentScale = ContentScale.Crop,
            model = post.first_image,
            contentDescription = null,

        )
        Box(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, Color.Black),
                    startY = 0f,
                    endY = 700f
                )
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()

//                .padding(start = 8.dp, end = 24.dp)
                .clip(
                    RoundedCornerShape(28.dp)
                )
                .clickable {

                    val finalUri = post.edit_url.removeSuffix(".md").drop(54)
                    println(finalUri)
                    onClick(finalUri)
                },
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Text(modifier = Modifier.padding(start = 14.dp,end = 8.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onBackground,
                text = post.title,
                maxLines = 1,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis
                )
            Text(modifier = Modifier.padding(start = 14.dp, bottom = 8.dp, end = 8.dp),
                fontSize = 14.sp,
                lineHeight = 14.sp,
                color = MaterialTheme.colorScheme.onBackground,
                text = post.preview,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,

            )
        }

    }

}