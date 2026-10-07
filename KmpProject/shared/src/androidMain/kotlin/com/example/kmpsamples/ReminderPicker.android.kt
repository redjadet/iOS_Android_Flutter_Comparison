package com.example.kmpsamples

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.datetime.LocalDateTime

@Composable
actual fun rememberReminderPickerHandler(): (LocalDateTime, (LocalDateTime) -> Unit) -> Unit {
    val context = LocalContext.current
    return remember(context) {
        { current, onSelected ->
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    // Commit only after both dialogs succeed so Cancel leaves state unchanged.
                    TimePickerDialog(
                        context,
                        { _, hour, minute ->
                            onSelected(
                                LocalDateTime(
                                    year = year,
                                    monthNumber = month + 1,
                                    dayOfMonth = day,
                                    hour = hour,
                                    minute = minute,
                                ),
                            )
                        },
                        current.hour,
                        current.minute,
                        false,
                    ).show()
                },
                current.year,
                current.monthNumber - 1,
                current.dayOfMonth,
            ).show()
        }
    }
}
