package com.acteam.vocago.presentation.screen.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.main.chat.component.LocationCard
import com.acteam.vocago.presentation.screen.main.chat.component.UserProfileCard
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveSP
import kotlin.math.absoluteValue

@Composable
fun ChatScreen(
    rootNavController: NavController,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val visibleLocationCardPage = remember { mutableStateOf<Int?>(null) }

    val deviceType = getDeviceType()

    val userProfileList = listOf(
        UserProfileData(
            R.drawable.trum_avatar, "Online", "Trump",
            stringResource(R.string.text_us_president),
            3,
            location = stringResource(R.string.text_white_house),
            country = "USA",
            locationImageRes = R.drawable.nhatrang,
            videoResId = null
        ) { id ->
            rootNavController.navigate(
                NavScreen.CommonChatNavScreen(
                    id,
                    "Trump",
                    R.drawable.trum_avatar
                )
            )
        },

        UserProfileData(
            R.drawable.dev_chatbot, "Online", "The Anh",
            stringResource(R.string.text_developer),
            4,
            location = stringResource(R.string.text_eastern_england),
            country = stringResource(R.string.text_vietnam),
            locationImageRes = R.drawable.donganh,
            videoResId = null
        ) { id ->
            rootNavController.navigate(
                NavScreen.CommonChatNavScreen(
                    id,
                    "The Anh",
                    R.drawable.dev_chatbot
                )
            )
        },

        UserProfileData(
            R.drawable.daily_chatbot, "Online", "Van Cong",
            stringResource(R.string.text_everyday_friend),
            5,
            location = "Berlin",
            country = stringResource(R.string.text_vietnam),
            locationImageRes = R.drawable.songcau,
            videoResId = null
        ) { id ->
            rootNavController.navigate(
                NavScreen.CommonChatNavScreen(
                    id,
                    "Van Cong",
                    R.drawable.daily_chatbot
                )
            )
        },
        UserProfileData(
            R.drawable.dack_avatar, "Online", "Jack",
            stringResource(R.string.text_singer),
            8,
            location = stringResource(R.string.text_bentre),
            country = stringResource(R.string.text_vietnam),
            locationImageRes = R.drawable.bentre,
            videoResId = R.raw.jack_videocall
        ) { id ->
            rootNavController.navigate(
                NavScreen.CommonChatNavScreen(
                    id,
                    "Jack",
                    R.drawable.dack_avatar
                )
            )
        },
        UserProfileData(
            R.drawable.ronaldo_avt, "Online", "Ronaldo",
            stringResource(R.string.text_football_boat),
            1,
            location = "Riyard",
            country = "Saudi Arabia",
            locationImageRes = R.drawable.ronaldo_location,
            videoResId = R.raw.ronaldo_videocall
        ) { id ->
            rootNavController.navigate(
                NavScreen.CommonChatNavScreen(
                    id,
                    "Ronaldo",
                    R.drawable.ronaldo_avt
                )
            )
        },

        UserProfileData(
            R.drawable.messi_avatar, "Online", "Messi",
            stringResource(R.string.text_football_goat),
            2,
            location = "Miami",
            country = "USA",
            locationImageRes = R.drawable.messi_location,
            videoResId = R.raw.messi_callvideo
        ) { id ->
            rootNavController.navigate(
                NavScreen.CommonChatNavScreen(
                    id,
                    "Messi",
                    R.drawable.messi_avatar
                )
            )
        },

        UserProfileData(
            R.drawable.tutor_chatbot, "Online", "Minh Nhat",
            stringResource(R.string.text_english_tutor),
            6,
            location = stringResource(R.string.text_hoguom),
            country = stringResource(R.string.text_vietnam),
            locationImageRes = R.drawable.hoguom,
            videoResId = null
        ) { id ->
            rootNavController.navigate(
                NavScreen.CommonChatNavScreen(
                    id,
                    "Minh Nhat",
                    R.drawable.tutor_chatbot
                )
            )
        },

        UserProfileData(
            R.drawable.zuckerberg_avatar, "Online", "Zuckerberg",
            stringResource(R.string.text_facebook_founder),
            7,
            location = "California",
            country = "USA",
            locationImageRes = R.drawable.cali,
            videoResId = null
        ) { id ->
            rootNavController.navigate(
                NavScreen.CommonChatNavScreen(
                    id,
                    "Zuckerberg",
                    R.drawable.zuckerberg_avatar
                )
            )
        },


        UserProfileData(
            R.drawable.faker_avatar, "Online", "Faker",
            stringResource(R.string.text_game_player),
            9,
            location = "Seoul",
            country = stringResource(R.string.text_hanquoc),
            locationImageRes = R.drawable.seoul,
            videoResId = null
        ) { id ->
            rootNavController.navigate(
                NavScreen.CommonChatNavScreen(
                    id,
                    "Faker",
                    R.drawable.faker_avatar
                )
            )
        },
    )

    val pagerState = rememberPagerState(
        initialPage = userProfileList.size / 2
    ) { userProfileList.size }

    val cardWidth = when (deviceType) {
        DeviceType.TabletLandscape -> screenWidth * 0.5f
        DeviceType.TabletPortrait -> screenWidth * 0.7f
        else -> screenWidth * 0.75f
    }
    val cardHeight = screenHeight * 0.6f
    val horizontalPadding = (screenWidth - cardWidth) / 2
    val cardPositions = remember {
        mutableStateListOf<IntOffset>().apply {
            repeat(userProfileList.size) { add(IntOffset.Zero) }
        }
    }
    Column {
        Header()

        if (deviceType == DeviceType.TabletPortrait) {
            Spacer(modifier = Modifier.height(64.dp))
        } else {
            Spacer(modifier = Modifier.height(24.dp))
        }

        Text(
            text = stringResource(R.string.text_selection_for_you),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 40.sp,
                color = MaterialTheme.colorScheme.primary
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        if (deviceType == DeviceType.TabletPortrait) {
            Spacer(modifier = Modifier.height(64.dp))
        } else {
            Spacer(modifier = Modifier.height(12.dp))
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(visibleLocationCardPage.value) {
                    detectTapGestures(onTap = {
                        visibleLocationCardPage.value = null
                    })
                }
        ) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                pageSpacing = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight)
            ) { page ->

                val pageOffset =
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                val absOffset = pageOffset.absoluteValue.coerceIn(0f, 1f)

                val scale = lerp(0.85f, 1f, 1f - absOffset)
                val alpha = lerp(0.5f, 1f, 1f - absOffset)
                val rotationY = lerp(if (pageOffset < 0) 15f else -15f, 0f, 1f - absOffset)
                val density = LocalDensity.current.density

                UserProfileCard(
                    imageResId = userProfileList[page].imageResId,
                    status = userProfileList[page].status,
                    name = userProfileList[page].name,
                    jobTitle = userProfileList[page].jobTitle,
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                            this.rotationY = rotationY
                            this.cameraDistance = 12 * density
                        }
                        .width(cardWidth)
                        .height(cardHeight),
                    onclick = { userProfileList[page].onclick(userProfileList[page].chatId) },
                    onToggleLocationClick = {
                        visibleLocationCardPage.value =
                            if (visibleLocationCardPage.value == page) null else page
                    },
                    onVideoCallClick = {
                        rootNavController.navigate(
                            NavScreen.VideoCallNavScreen(
                                receivedName = userProfileList[page].name,
                                avatarResId = userProfileList[page].imageResId,
                                videoResId = userProfileList[page].videoResId
                            )
                        )
                    }
                )
            }

            visibleLocationCardPage.value?.let { page ->
                val pos = cardPositions.getOrNull(page) ?: IntOffset.Zero
                var offsetY = 0
                offsetY = if (deviceType == DeviceType.TabletLandscape) {
                    with(LocalDensity.current) { (cardHeight * 0.15f).toPx().toInt() }
                } else {
                    with(LocalDensity.current) { (cardHeight * 0.25f).toPx().toInt() }
                }
                var offsetXExtra = 0
                offsetXExtra = if (deviceType == DeviceType.Mobile) {
                    with(LocalDensity.current) { 48.dp.toPx().toInt() }
                } else if (deviceType == DeviceType.TabletPortrait) {
                    with(LocalDensity.current) { 120.dp.toPx().toInt() }
                } else {
                    with(LocalDensity.current) { 320.dp.toPx().toInt() }
                }
                LocationCard(
                    location = userProfileList[page].location,
                    country = userProfileList[page].country,
                    imageRes = userProfileList[page].locationImageRes,
                    modifier = Modifier
                        .offset { IntOffset(pos.x + offsetXExtra, (pos.y + offsetY).toInt()) }
                        .width(cardWidth)
                        .zIndex(10f)
                        .clickable(
                            enabled = true,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            // bấm trong LocationCard thì không ẩn
                        }
                )
            }

        }

    }
}


data class UserProfileData(
    val imageResId: Int,
    val status: String,
    val name: String,
    val jobTitle: String,
    val chatId: Int,
    val location: String,
    val country: String,
    val locationImageRes: Int,
    val videoResId: Int?,
    val onclick: (Int) -> Unit,
)


@Composable
fun Header(
) {

    val deviceType = getDeviceType()

    val headerHeight = when (deviceType) {
        DeviceType.Mobile -> 60.dp
        DeviceType.TabletPortrait -> 90.dp
        DeviceType.TabletLandscape -> 80.dp
    }


    val horizontalPadding = when (deviceType) {
        DeviceType.Mobile -> 16.dp
        DeviceType.TabletPortrait -> 32.dp
        DeviceType.TabletLandscape -> 24.dp
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = horizontalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Chatbot",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = responsiveSP(
                        mobile = 20,
                        tabletPortrait = 22,
                        tabletLandscape = 24
                    ),
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.text_practice_communicate_with_chatbot),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Start
            )
        }
    }
}
