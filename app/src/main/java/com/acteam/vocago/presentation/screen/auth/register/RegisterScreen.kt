package com.acteam.vocago.presentation.screen.auth.register

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acteam.vocago.R
import com.acteam.vocago.presentation.screen.auth.common.AuthImageCard
import com.acteam.vocago.presentation.screen.auth.common.BackButton
import com.acteam.vocago.presentation.screen.auth.register.component.RegisterForm
import com.acteam.vocago.utils.DeviceType
import com.acteam.vocago.utils.getDeviceType
import com.acteam.vocago.utils.responsiveDP
import com.acteam.vocago.utils.responsiveSP
import com.acteam.vocago.utils.safeClickable

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val titleFontSize = responsiveSP(mobile = 30, tabletPortrait = 36, tabletLandscape = 42)
    val horizontalPadding = responsiveDP(mobile = 24, tabletPortrait = 40, tabletLandscape = 48)
    val verticalSpacing = responsiveDP(mobile = 12, tabletPortrait = 20, tabletLandscape = 24)
    val buttonHeight = responsiveDP(mobile = 48, tabletPortrait = 56, tabletLandscape = 60)
    val descFontSize = responsiveSP(mobile = 14, tabletPortrait = 20, tabletLandscape = 20)

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            if (getDeviceType() == DeviceType.TabletPortrait) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = horizontalPadding)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(verticalSpacing)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BackButton(
                            onClick = onBackClick,
                        )
                        Row(
                            modifier = Modifier
                                .weight(1f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.btn_sign_up),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = titleFontSize
                                ),
                                textAlign = TextAlign.Center,
                            )
                        }
                        Spacer(modifier = Modifier.width(40.dp))
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AuthImageCard(R.drawable.register, 0.5f)
                    }
                    RegisterForm()

                    Button(
                        onClick = onRegisterClick,
                        modifier = Modifier
                            .height(buttonHeight)
                            .fillMaxWidth()
                            .shadow(8.dp, shape = RoundedCornerShape(24.dp))
                    ) {
                        Text(
                            text = stringResource(R.string.btn_sign_up).uppercase(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.text_already_have_account),
                            fontSize = descFontSize
                        )
                        Text(
                            text = stringResource(R.string.btn_login),
                            modifier = Modifier
                                .safeClickable("btn_login", onClick = onBackClick)
                                .padding(start = 8.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                                fontSize = descFontSize
                            )
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = horizontalPadding)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(verticalSpacing)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BackButton(
                            onClick = onBackClick,
                        )
                        Row(
                            modifier = Modifier
                                .weight(1f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.btn_sign_up),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = titleFontSize,
                                    color = MaterialTheme.colorScheme.primary
                                ),
                                textAlign = TextAlign.Center,
                            )
                        }
                        Spacer(modifier = Modifier.width(40.dp))
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )

                    RegisterForm()

                    Spacer(modifier = Modifier.height(verticalSpacing / 3))

                    Button(
                        onClick = onRegisterClick,
                        modifier = Modifier
                            .height(buttonHeight)
                            .fillMaxWidth()
                            .shadow(8.dp, shape = RoundedCornerShape(24.dp))
                    ) {
                        Text(
                            text = stringResource(R.string.btn_sign_up).uppercase(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.text_already_have_account),
                            fontSize = descFontSize
                        )
                        Text(
                            text = stringResource(R.string.btn_login),
                            modifier = Modifier
                                .safeClickable("btn_login", onClick = onBackClick)
                                .padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                                fontSize = descFontSize
                            )
                        )
                    }
                }
            }
        }
    }

}
