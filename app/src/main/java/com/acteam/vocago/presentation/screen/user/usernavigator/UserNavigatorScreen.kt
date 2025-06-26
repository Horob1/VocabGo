package com.acteam.vocago.presentation.screen.user.usernavigator

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.presentation.navigation.NavScreen
import com.acteam.vocago.presentation.screen.common.LoadingSurface
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.user.common.UserTopBar
import com.acteam.vocago.presentation.screen.user.usernavigator.component.NavigatorButton
import com.acteam.vocago.presentation.screen.user.usernavigator.component.UserCard
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveDP

@Composable
fun UserNavigatorScreen(
    rootNavController: NavController,
    userNavController: NavController,
    viewModel: UserNavigatorViewModel,
) {
    val context = LocalContext.current
    val buttonList = remember {
        listOf(
            Triple(
                {
                    userNavController.navigate(
                        NavScreen.ProfileNavScreen
                    )
                },
                R.string.title_account,
                Icons.Filled.Person
            ),
//            Triple(
//                {
//                    userNavController.navigate(
//                        NavScreen.BillingNavScreen
//                    )
//                },
//                R.string.title_billing_payment,
//                Icons.Filled.Payments
//            ),
            Triple(
                {
                    userNavController.navigate(
                        NavScreen.AlarmNavScreen
                    )
                },
                R.string.title_alarm,
                Icons.Filled.Alarm
            ),
            Triple(
                {
                    rootNavController.navigate(
                        NavScreen.SettingNavScreen
                    )
                },
                R.string.title_setting,
                Icons.Filled.Settings
            )
        )
    }
    val user by viewModel.userState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val clientType = getDeviceType()


    Column(
        modifier = Modifier
            .padding(
                horizontal = responsiveDP(
                    mobile = 16,
                    tabletPortrait = 24,
                    tabletLandscape = 32
                )
            )
            .fillMaxSize()
    ) {
        UserTopBar(
            onBackClick = {
                rootNavController.popBackStack()
            },
            titleId = R.string.title_profile
        )

        if (clientType != DeviceType.TabletLandscape) {
            Spacer(Modifier.height(8.dp))

            UserCard(
                modifier = Modifier.fillMaxWidth(),
                user = user
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                items(buttonList.size) {
                    val buttonInfo = buttonList[it]
                    NavigatorButton(
                        onClick = buttonInfo.first,
                        stringId = buttonInfo.second,
                        icon = buttonInfo.third
                    )
                }

            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserCard(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    user = user
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    items(buttonList.size) {
                        val buttonInfo = buttonList[it]
                        NavigatorButton(
                            onClick = buttonInfo.first,
                            stringId = buttonInfo.second,
                            icon = buttonInfo.third
                        )
                    }

                }

            }
        }
        Button(
            modifier = Modifier
                .height(responsiveDP(48, 56, 60))
                .fillMaxWidth()
                .shadow(8.dp, shape = RoundedCornerShape(24.dp)),
            onClick = {
                viewModel.logout {
                    rootNavController.popBackStack()
                }
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "logout"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(stringResource(R.string.title_logout))
            }
        }

    }


    if (uiState is UIState.UILoading) {
        LoadingSurface()
    }

    if (uiState is UIState.UIError) {
        Toast.makeText(context, context.getString(R.string.text_unknown_error), Toast.LENGTH_SHORT)
            .show()
        viewModel.clearUiState()
    }
}

@Composable
fun RowInTabletLandscape(
    content: @Composable () -> Unit,
) {
    val clientType = getDeviceType()
    if (clientType == DeviceType.TabletLandscape) Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        content()
    } else content()
}