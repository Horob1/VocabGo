package com.acteam.vocago.presentation.screen.welcome.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.AppLanguage
import com.acteam.vocago.presentation.screen.welcome.WelcomeViewModel
import com.acteam.vocago.presentation.screen.welcome.data.ChooseLanguageData
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.LanguageUtils
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP

@Composable
fun ChooseLanguagePage(
    viewModel: WelcomeViewModel
) {
    val context = LocalContext.current
    val languages = listOf(
        ChooseLanguageData.System,
        ChooseLanguageData.English,
        ChooseLanguageData.Vietnamese
    )
    val appLanguage = viewModel.appLanguage.collectAsState()
    var oldLanguage by remember { mutableStateOf<AppLanguage>(appLanguage.value) }
    var isShowDialog by remember { mutableStateOf(false) }
    val image = when (appLanguage.value) {
        AppLanguage.English -> R.drawable.capy_uk
        AppLanguage.Vietnamese -> R.drawable.capy_vi
        else -> {
            R.drawable.capy_world
        }
    }

    val titleFontSize = responsiveSP(mobile = 18, tabletPortrait = 24, tabletLandscape = 28)
    val horizontalPadding = responsiveDP(mobile = 24, tabletPortrait = 40, tabletLandscape = 48)
    val topPadding = responsiveDP(mobile = 16, tabletPortrait = 24, tabletLandscape = 32)
    val verticalSpacing = responsiveDP(mobile = 12, tabletPortrait = 20, tabletLandscape = 24)

    val deviceType = getDeviceType()

    if (deviceType == DeviceType.Mobile || deviceType == DeviceType.TabletPortrait) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = verticalSpacing)
        ) {
            Spacer(modifier = Modifier.height(verticalSpacing))

            OnBoardingImageCard(image, 0.9f, if (deviceType == DeviceType.Mobile) 0.5f else 0.6f)

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.onboarding_title_4),
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = horizontalPadding),
                verticalArrangement = Arrangement.spacedBy(
                    space = verticalSpacing
                )
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
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = topPadding)
                .padding(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OnBoardingImageCard(image, 0.5f, 0.8f)

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.onboarding_title_4),
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(verticalSpacing))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding),
                    verticalArrangement = Arrangement.spacedBy(space = verticalSpacing)
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

    if (isShowDialog)
        ChangeLanguageDialog({
            isShowDialog = false
            viewModel.changeLanguage(oldLanguage)
        }, {
            isShowDialog = false
            viewModel.saveLanguage()
            LanguageUtils.changeLanguage(
                context,
                appLanguage.value.languageCode
            )
        })
}
