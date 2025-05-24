package com.acteam.vocago.presentation.screen.welcome.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import com.acteam.vocago.R
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP
import com.acteam.vocago.utils.responsiveValue

@Composable
fun ChangeLanguageDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val horizontalPadding = responsiveDP(mobile = 24, tabletPortrait = 40, tabletLandscape = 48)
    val titleFontSize = responsiveSP(mobile = 18, tabletPortrait = 24, tabletLandscape = 28)
    val descFontSize = responsiveSP(mobile = 14, tabletPortrait = 18, tabletLandscape = 20)


    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 8.dp,
            modifier = Modifier.wrapContentSize()
        ) {
            Column {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            responsiveValue(
                                260,
                                360,
                                400
                            ).dp
                        )
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(R.drawable.screen_flicker),
                        contentDescription = "Pager Image",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.txt_screen_may_be_flicker),
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                        .padding(top = 20.dp),
                    text = stringResource(id = R.string.txt_screen_may_be_flicker_detail),
                    fontSize = descFontSize,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(id = R.string.btn_cancel))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    TextButton(onClick = onConfirm) {
                        Text(stringResource(id = R.string.btn_continue))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
