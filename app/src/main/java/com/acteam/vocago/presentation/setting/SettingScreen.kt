package com.acteam.vocago.presentation.setting

import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.AppTheme
import com.acteam.vocago.presentation.screen.welcome.component.ChangeLanguageDialog
import com.acteam.vocago.presentation.screen.welcome.component.ChooseLanguageButton
import com.acteam.vocago.presentation.screen.welcome.data.ChooseLanguageData
import com.acteam.vocago.presentation.setting.component.DarkModeSwitch
import com.acteam.vocago.utils.LanguageUtils
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    rootNavController: NavController,
) {
    val context = LocalContext.current
    val theme by viewModel.theme.collectAsState()
    val dynamicColor by viewModel.dynamicColor.collectAsState()
    val appLanguage = viewModel.appLanguage.collectAsState()
    var oldLanguage by remember { mutableStateOf(appLanguage.value) }
    var isShowChooseLang by remember { mutableStateOf(false) }
    var isShowDialog by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isShowChooseLang) 90f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "icon rotation"
    )

    val languages = listOf(
        ChooseLanguageData.System,
        ChooseLanguageData.English,
        ChooseLanguageData.Vietnamese
    )

    val longHeightDp = responsiveDP(
        mobile = 24,
        tabletPortrait = 30,
        tabletLandscape = 36
    )

    val shortHeightDp = responsiveDP(
        mobile = 8,
        tabletPortrait = 12,
        tabletLandscape = 16
    )

    val horizontalPadding = responsiveDP(
        mobile = 16,
        tabletPortrait = 24,
        tabletLandscape = 32
    )

    val superTitleFontSize = responsiveSP(
        mobile = 20,
        tabletPortrait = 24,
        tabletLandscape = 28
    )

    val titleFontSize = responsiveSP(
        mobile = 16,
        tabletPortrait = 18,
        tabletLandscape = 20
    )

    val descriptionFontSize = responsiveSP(
        mobile = 12,
        tabletPortrait = 14,
        tabletLandscape = 16
    )

    val titleStyle = MaterialTheme.typography.titleMedium.copy(
        fontSize = titleFontSize,
        color = MaterialTheme.colorScheme.onSurface
    )

    val descriptionStyle = MaterialTheme.typography.bodyMedium.copy(
        fontSize = descriptionFontSize,
        color = MaterialTheme.colorScheme.onSurface
    )


    Scaffold {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = horizontalPadding
                )
                .fillMaxSize()
                .padding(it)
        ) {
            Row(
                Modifier
                    .padding(
                        vertical = shortHeightDp
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        rootNavController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }


                Text(
                    stringResource(R.string.title_setting),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = superTitleFontSize,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    ),
                )

                Spacer(
                    modifier = Modifier
                        .padding(
                            8.dp
                        )
                        .width(20.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(longHeightDp)
            )

            Column(
                modifier = Modifier
                    .padding(
                        vertical = shortHeightDp,
                        horizontal = horizontalPadding
                    )
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.title_theme),
                    style = titleStyle,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(shortHeightDp)
                )

                ColumnSettingCustom {
                    RowSettingCustom {
                        Column(
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                stringResource(R.string.title_follow_system_theme),
                                style = titleStyle,
                            )
                            Text(
                                stringResource(R.string.desc_follow_system),
                                style = descriptionStyle,
                            )
                        }

                        Switch(
                            checked = theme is AppTheme.SYSTEM,
                            onCheckedChange = {
                                if (theme is AppTheme.SYSTEM)
                                    viewModel.setTheme(AppTheme.LIGHT)
                                else
                                    viewModel.setTheme(AppTheme.SYSTEM)
                            }
                        )
                    }

                    HorizontalDivider()

                    RowSettingCustom {
                        Column(
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                stringResource(R.string.title_dark_mode),
                                style = titleStyle,
                            )
                            Text(
                                stringResource(R.string.desc_dark_mode),
                                style = descriptionStyle,
                            )
                        }
                        DarkModeSwitch(
                            checked = if (theme is AppTheme.SYSTEM) isSystemInDarkTheme() else theme is AppTheme.DARK,
                            modifier = Modifier,
                            onCheckedChanged = {
                                when (theme) {
                                    AppTheme.LIGHT -> viewModel.setTheme(AppTheme.DARK)
                                    AppTheme.DARK -> viewModel.setTheme(AppTheme.LIGHT)
                                    else -> {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.toast_please_turn_off_the_sync_system_them),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        )
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        HorizontalDivider()

                        RowSettingCustom {
                            Column(
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    stringResource(R.string.title_dynamic_color),
                                    style = titleStyle,
                                )
                                Text(
                                    stringResource(R.string.desc_dynamic_color),
                                    style = descriptionStyle,
                                )
                            }
                            Switch(
                                checked = dynamicColor,
                                onCheckedChange = {
                                    viewModel.setDynamicColor(!dynamicColor)
                                }
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(longHeightDp)
                )

                Text(
                    stringResource(R.string.title_locales),
                    style = titleStyle,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(shortHeightDp)
                )

                ColumnSettingCustom {
                    RowSettingCustom {
                        Column(
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                stringResource(R.string.title_language),
                                style = titleStyle,
                            )
                            Text(
                                stringResource(R.string.desc_language),
                                style = descriptionStyle,
                            )
                        }
                        IconButton(
                            onClick = {
                                isShowChooseLang = !isShowChooseLang
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = "Show drop down menu",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.rotate(rotation)
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = isShowChooseLang,
                        enter = expandVertically(animationSpec = tween(300)),
                        exit = shrinkVertically(animationSpec = tween(300))
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(languages.size) { index ->
                                val lang = languages[index]
                                ChooseLanguageButton(
                                    lang.flag,
                                    lang.languageName,
                                    lang.language == appLanguage.value
                                ) {
                                    isShowDialog = true
                                    oldLanguage = appLanguage.value
                                    viewModel.changeLanguage(lang.language)
                                }
                            }
                        }
                    }
                }
            }

        }

        if (isShowDialog)
            ChangeLanguageDialog({
                isShowDialog = false

            }, {
                isShowDialog = false

                LanguageUtils.changeLanguage(
                    context,
                    appLanguage.value.languageCode
                )
            })
    }
}

@Composable
fun ColumnSettingCustom(
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier

            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.shapes.medium
            )
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            )
            .clip(
                MaterialTheme.shapes.medium
            ),
    ) {
        content()
    }
}

@Composable
fun RowSettingCustom(
    content: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(
                vertical = 8.dp
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}