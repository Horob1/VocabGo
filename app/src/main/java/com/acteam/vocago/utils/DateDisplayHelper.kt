package com.acteam.vocago.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.acteam.vocago.R
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateDisplayHelper {

    fun formatDateString(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return "Invalid date"

        val inputFormats = listOf(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            },
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            },
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        )

        val date: Date? = inputFormats.asSequence()
            .mapNotNull { format ->
                try {
                    format.parse(dateString)
                } catch (e: Exception) {
                    null
                }
            }.firstOrNull()

        return date?.let {
            val outputFormat = SimpleDateFormat("HH.mm dd.MM.yyyy", Locale.getDefault())
            outputFormat.format(it)
        } ?: "Invalid date"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ConvertToTimeAgo(dateTime: String) {
        val parsedDateTime = OffsetDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        val currentDateTime = OffsetDateTime.now()

        val duration = Duration.between(parsedDateTime, currentDateTime)

        return when {
            duration.toMinutes() < 1 -> Text(
                text = stringResource(R.string.just_now),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            duration.toMinutes() < 60 -> Text(
                text = "${duration.toMinutes()} ${
                    stringResource(
                        R.string.minute_ago
                    )
                }",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant

            )

            duration.toHours() < 24 ->
                Text(
                    text = "${duration.toHours()} ${
                        stringResource(
                            R.string.hour_ago
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            duration.toDays() < 7 ->
                Text(
                    text = "${duration.toDays()} ${
                        stringResource(
                            R.string.day_ago
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            duration.toDays() < 30 ->
                Text(
                    text = "${duration.toDays() / 7} ${
                        stringResource(
                            R.string.week_ago
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            duration.toDays() < 365 ->
                Text(
                    text = "${duration.toDays() / 30} ${
                        stringResource(
                            R.string.month_ago
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            else ->
                Text(
                    text = "${duration.toDays() / 365} ${
                        stringResource(
                            R.string.year_ago
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
        }
    }

}