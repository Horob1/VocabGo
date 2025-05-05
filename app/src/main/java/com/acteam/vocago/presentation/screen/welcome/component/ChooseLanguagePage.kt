package com.acteam.vocago.presentation.screen.welcome.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.AppLanguage
import com.acteam.vocago.presentation.screen.welcome.WelcomeViewModel
import com.acteam.vocago.presentation.screen.welcome.data.ChooseLanguageData
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.LanguageUtils
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveFontSize
import com.acteam.vocago.utils.responsivePadding
import com.acteam.vocago.utils.responsiveSpacing

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

    // Responsive font sizes, paddings, and spacing
    val titleFontSize = responsiveFontSize(mobile = 18, tablet = 24)
    val horizontalPadding = responsivePadding(mobile = 24, tablet = 40)
    val topPadding = responsivePadding(mobile = 16, tablet = 24)
    val verticalSpacing = responsiveSpacing(mobile = 12, tablet = 20)

    val deviceType = getDeviceType()

    // Mobile layout: full column
    if (deviceType == DeviceType.Mobile) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(space = verticalSpacing)
        ) {
            Spacer(modifier = Modifier.height(verticalSpacing))
            OnBoardingImageCard(image, 0.9f, 0.5f)

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
                    space = responsiveSpacing(
                        mobile = 12,
                        tablet = 24
                    )
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
        // Tablet layout: Row with image and language list
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = topPadding)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image card on the left
            OnBoardingImageCard(image, 0.5f, 0.8f)

            // Column for text and LazyColumn on the right
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
