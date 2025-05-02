package com.acteam.vocago.presentation.screen.welcome.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.acteam.vocago.utils.LanguageUtils

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
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 12.dp
        )
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        OnBoardingImageCard(image, 0.9f, 0.5f)
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(R.string.onboarding_title_4),
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 18.dp
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
}