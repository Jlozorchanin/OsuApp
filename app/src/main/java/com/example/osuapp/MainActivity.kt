package com.example.osuapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.compose.OsuAppTheme
import com.example.osuapp.components.Details
import com.example.osuapp.components.FAQScreen
import com.example.osuapp.components.FriendDetails
import com.example.osuapp.components.NewsItem
import com.example.osuapp.components.ProfileScreen
import com.example.osuapp.components.WelcomeScreen
import com.example.osuapp.viewmodels.ApiVM
import com.example.osuapp.viewmodels.ReqDataState
import com.example.osuapp.viewmodels.Screens
import com.example.osuapp.viewmodels.TokenState
import com.example.osuapp.viewmodels.UiViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        setContent {
            OsuAppTheme(darkTheme = true) {
                val apiVM by viewModels<ApiVM>()
                val uiVM by viewModels<UiViewModel>()
                Surface {
                    val provider = DataStoreProvider(LocalContext.current)
                    val scope = rememberCoroutineScope()
                    val lazyGridState = rememberLazyGridState()
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    var friendsScreenExpanded by remember {
                        mutableStateOf(true)
                    }


                    val lazyColumnState = rememberLazyListState()
                    val lazyColumnFriendsState = rememberLazyListState()
                    val tokenValue = apiVM.tokenState.collectAsState()
                    val requestsDataState = apiVM.requestsState.collectAsState()
                    val userState = apiVM.userDataState.collectAsState()
                    val uiState = uiVM.uiState.collectAsState()
                    var codeValue by remember {
                        mutableStateOf("")
                    }
                    var currentScreen by remember {
                        mutableStateOf("welcome") // faq | welcome
                    }
                    var isTokenMissing by remember {
                        mutableStateOf(true)
                    }
                    if(intent.action == Intent.ACTION_VIEW && intent.data?.host == "clovertestcode.online"){
                        codeValue = intent.data?.getQueryParameter("code")?: ""
                        isTokenMissing = false
                        runBlocking {  provider.saveInfo(false)}
                    }

                    LaunchedEffect(key1 = true) {
                        isTokenMissing = provider.getInfo.first()
                    }


                    Scaffold(
                        topBar = {
                            CenterAlignedTopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                                title = {
//                                    SearchBar()
                                    AnimatedVisibility(visible = userState.value.data?.username != null,
                                        enter = fadeIn(tween(200))) {
                                        Text(text = userState.value.data?.username ?: "", maxLines = 1, overflow = TextOverflow.Clip)
                                    }
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        currentScreen = "faq"
                                        uiVM.changeScreen(Screens.FAQ,uiState.value.recentScreen) }) {
                                        Icon(
                                            modifier = Modifier.size(32.dp),
                                            imageVector = Icons.Filled.Info,
                                            contentDescription = null
                                        )
                                    }
                                },
                                actions = {
                                    AnimatedVisibility(visible = userState.value.data?.avatar_url != null,
                                        enter = fadeIn(tween(200))
                                    ){
                                        AsyncImage(

                                            modifier = Modifier
                                                .padding(end = 12.dp)
                                                .clip(RoundedCornerShape(50.dp))
                                                .size(55.dp)
                                                .clickable {
                                                    uiVM.changeScreen(Screens.PROFILE, Screens.HOME)
                                                },
                                            model = userState.value.data?.avatar_url ?: R.drawable.profile_bacjpeg,
                                            contentDescription = null
                                        )
                                    }

                                }
                            )
                        },
                        content = { innerPadding ->
                            if (!isTokenMissing) {
                                when (uiState.value.screen){
                                    Screens.HOME ->{
                                        AnimatedVisibility(visible = uiState.value.screen==Screens.HOME, enter = fadeIn(animationSpec = tween(200)), exit = fadeOut(
                                            tween(300)
                                        )) {
                                            MainScreen(
                                                modifier = Modifier.padding(innerPadding),
                                                reqState = requestsDataState,
                                                provider = provider,
                                                lazyGridState = lazyGridState,
                                                tokenState = tokenValue,
                                                back = {
                                                    if (uiVM.uiState.value.recentScreen !=Screens.HOME){
                                                        uiVM.changeScreen(uiState.value.recentScreen,Screens.HOME)
                                                    }

                                                },
                                                getToken = {
                                                    if (codeValue == "") {
                                                        isTokenMissing = true
                                                    } else {
                                                        apiVM.getTokenFromCode(codeValue) {
                                                            scope.launch {
                                                                provider.saveBaseData(
                                                                    time = (System.currentTimeMillis() / 1000).toInt(),
                                                                    tokenValue = it.access_token,
                                                                    refreshTokenValue = it.refresh_token
                                                                ).also {
                                                                    println(
                                                                        tokenValue.value.token?.access_token ?: "no token"
                                                                    )
                                                                }
                                                            }
                                                        }

                                                    }
                                                },
                                                refreshToken = {
                                                    apiVM.updateTokenValue(it) {
                                                        scope.launch {
                                                            provider.saveBaseData(
                                                                time = (System.currentTimeMillis() / 1000).toInt(),
                                                                tokenValue = it.access_token,
                                                                refreshTokenValue = it.refresh_token
                                                            ).also {
                                                                println(
                                                                    tokenValue.value.token?.refresh_token ?: "no ref token"
                                                                )
                                                            }
                                                        }
                                                    }
                                                },
                                                refreshVMValue = { token, refresh ->
                                                    apiVM.updateDataFromDataStore(token, refresh)
                                                },
                                                getData = {
                                                    apiVM.getBaseData()
                                                },
                                                getAddInfo = {
                                                    apiVM.getScores()
                                                    apiVM.getFriends()
                                                }
                                            )
                                        }
                                    }

                                    Screens.PROFILE -> {
                                        AnimatedVisibility(visible = uiState.value.screen == Screens.PROFILE, enter = fadeIn(
                                            tween(400)
                                        )) {
                                            ProfileScreen(
                                                modifier = Modifier.padding(innerPadding),
                                                userState = userState,
                                                lazyListState = lazyColumnState,
                                                reqState = requestsDataState ,
                                                expanded = expanded,
                                                changeExpanded = {expanded = !expanded},
                                                back = {uiVM.changeScreen(Screens.HOME,Screens.PROFILE)},
                                                openDetails = {
                                                    uiVM.changeScore(it)
                                                    uiVM.changeScreen(Screens.EXTRA_MAP_DETAILS,Screens.PROFILE)
                                                },
                                                openFriendDetail = {
                                                    apiVM.getFriendData(it)
                                                    uiVM.changeScreen(Screens.FRIEND_DETAILS,Screens.PROFILE)
                                                }
                                            )

                                        }

                                    }

                                    Screens.FRIEND_DETAILS -> {
                                        FriendDetails(
                                            modifier = Modifier.padding(innerPadding),
                                            userState = requestsDataState,
                                            expanded = friendsScreenExpanded,
                                            columnState = lazyColumnFriendsState,
                                            back = {uiVM.changeScreen(Screens.PROFILE,Screens.PROFILE)},
                                            changeExpanded = {friendsScreenExpanded = !friendsScreenExpanded},
                                            openDetails = {
                                                uiVM.changeScore(it)
                                                uiVM.changeScreen(Screens.EXTRA_MAP_DETAILS,Screens.FRIEND_DETAILS)
                                            },
                                        )
                                    }
                                    Screens.EXTRA_MAP_DETAILS -> Details(
                                        modifier = Modifier.padding(innerPadding),
                                        userState = userState,
                                        reqState = requestsDataState,
                                        score = uiState.value.score!!
                                    ) {
                                        uiVM.changeScreen(uiState.value.recentScreen,uiState.value.recentScreen)
                                    }

                                    Screens.FAQ -> {
                                        FAQScreen(modifier = Modifier.padding(innerPadding), back = {uiVM.changeScreen(Screens.HOME,uiState.value.recentScreen) }) {
                                            val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            i.data = Uri.fromParts("package", packageName, null)
                                            startActivity(i)
                                        }
                                    }



                                }

                            }
                            else{

                                if (currentScreen == "welcome") {
                                    WelcomeScreen(
                                    goToFAQ = {
                                        currentScreen = "faq"
                                    }

                                    )
                                }
                                else if (currentScreen == "faq") {
                                    FAQScreen(modifier = Modifier.padding(top = 50.dp),
                                        back = {
                                            currentScreen = "welcome"
                                            uiVM.changeScreen(Screens.HOME,Screens.HOME)
                                        },
                                        launchSettings = {
                                            val i = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                            i.data = Uri.fromParts("package", packageName, null)
                                            startActivity(i)
                                        },
                                    )
                                }


                            }
                        }


                    )
                }
            }
        }

    }
}


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    reqState: State<ReqDataState>,
    tokenState : State<TokenState>,
    provider: DataStoreProvider,
    lazyGridState: LazyGridState,
    back : () -> Unit,
    getToken : () -> Unit,
    refreshToken : (String) -> Unit,
    getData : () -> Unit,
    refreshVMValue : (token : String, refreshToken : String) -> Unit,
    getAddInfo : () -> Unit
) {
    val localWidth = LocalConfiguration.current
//    val scope = rememberCoroutineScope()
    val uriHandler = LocalUriHandler.current

    BackHandler {
        back()
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .paint(painterResource(id = R.drawable.profile_bacjpeg), contentScale = ContentScale.Crop))
    LaunchedEffect(key1 = true) {
        val tokenTime = provider.getTokenTime.first()
        val systemTime = System.currentTimeMillis()/1000
        val tokenValue = provider.getToken.first()
        val refreshTokenValue = provider.getRefreshToken.first()

        if (systemTime.toInt() - tokenTime.toString().toInt() > 86000 && tokenValue == ""){
            getToken()
            getData()
            getAddInfo()
        }
        else if (systemTime.toInt() - tokenTime.toString().toInt() > 86000 && tokenValue != ""){
            refreshToken(refreshTokenValue)
            getData()
            getAddInfo()
        }
        else if (systemTime.toInt() - tokenTime.toString().toInt() < 86000 && tokenState.value.token?.access_token.isNullOrEmpty()){
            refreshVMValue(tokenValue,refreshTokenValue)
            println(tokenValue)
            println("mewow")
            getData()
            getAddInfo()
        }





    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){

        if (reqState.value.news?.news_posts != null){
            LazyVerticalGrid(
                columns = GridCells.Adaptive(500.dp),
                state = lazyGridState,
                modifier = Modifier.fillMaxSize(),

            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        contentAlignment = Alignment.TopStart
                    ){
                        Text(
                            text= "Недавние новости",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 26.sp,
                            modifier = Modifier.padding(start = localWidth.screenWidthDp.dp/30)
                        )

                    }
                }
                item {

                }
                items(reqState.value.news?.news_posts!!) {post ->
                    NewsItem(post = post){
                        uriHandler.openUri("https://osu.ppy.sh/home/news/$it")
                    }
                }

            }
        }

    }
    
}





