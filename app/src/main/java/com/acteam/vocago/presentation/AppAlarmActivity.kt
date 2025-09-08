package com.acteam.vocago.presentation

import android.media.MediaPlayer
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acteam.vocago.R
import com.acteam.vocago.domain.model.Alarm
import com.acteam.vocago.domain.usecase.GetAlarmByIdUseCase
import com.acteam.vocago.domain.usecase.UpdateAlarmUseCase
import com.meticha.triggerx.TriggerXActivity
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppAlarmActivity : TriggerXActivity(), KoinComponent {
    private val getAlarmByIdUseCase: GetAlarmByIdUseCase by inject()
    private val updateAlarmUseCase: UpdateAlarmUseCase by inject()

    @Composable
    override fun AlarmContent() {
        val context = LocalContext.current
        val bundle = remember { intent?.getBundleExtra("ALARM_DATA") }
        var alarm by remember { mutableStateOf<Alarm?>(null) }
        LaunchedEffect(Unit) {
            alarm = getAlarmByIdUseCase(bundle?.getInt("alarmId") ?: -1)
            alarm?.let {
                if (!it.isLoop) {
                    updateAlarmUseCase(alarm!!.copy(enabled = false))
                } else {
                    updateAlarmUseCase(alarm!!.copy(enabled = true))
                }
            }
        }

        BackHandler {}

        DisposableEffect(Unit) {
            val mediaPlayer = MediaPlayer.create(
                context,
                R.raw.alarm_sound
            )

            mediaPlayer.start()

            onDispose {
                mediaPlayer.stop()
                mediaPlayer.release()
            }

        }
        MaterialTheme {
            Scaffold {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary)
                            .blur(20.dp),
                    ) { }
                    if (alarm == null)
                        CircularProgressIndicator()
                    else AlarmScreenDetail(
                        alarm = alarm!!,
                        onClick = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AlarmScreenDetail(
    alarm: Alarm = Alarm(
        0, 0, 0, true, label = "Test", true
    ),
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            16.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.alarm_img),
            contentDescription = "Background Image",
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium),

            )

        Spacer(Modifier.height(8.dp))

        Text(
            text = alarm.label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 26.sp
            )
        )

        Text(
            "${alarm.hour}:${alarm.minute}",
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Black,
                fontSize = 86.sp
            )
        )



        Button(
            onClick = {
                onClick()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.btn_ok))
        }
    }

}

